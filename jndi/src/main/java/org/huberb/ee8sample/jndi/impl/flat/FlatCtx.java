/* 
 * "@(#)FlatCtx.java	1.1	00/01/18 SMI"
 * 
 * Copyright 1997, 1998, 1999 Sun Microsystems, Inc. All Rights
 * Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free,
 * license to use, modify and redistribute this software in source and
 * binary code form, provided that i) this copyright notice and license
 * appear on all copies of the software; and ii) Licensee does not 
 * utilize the software in a manner which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE 
 * HEREBY EXCLUDED.  SUN AND ITS LICENSORS SHALL NOT BE LIABLE 
 * FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, 
 * MODIFYING OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN 
 * NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER 
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT 
 * OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS 
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line
 * control of aircraft, air traffic, aircraft navigation or aircraft
 * communications; or in the design, construction, operation or
 * maintenance of any nuclear facility. Licensee represents and warrants
 * that it will not use or redistribute the Software for such purposes.  
 */
package org.huberb.ee8sample.jndi.impl.flat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;

/**
 * A sample service provider that implements a flat name space in memory.
 */
public class FlatCtx implements Context {

    protected static final NameParser myParser = new FlatNameParser();

    private final Hashtable myEnv;
    private final Map<String, Object> bindings;

    public FlatCtx(Hashtable inEnv) {
        this(inEnv, null);
    }

    private FlatCtx(Hashtable inEnv, Map<String, Object> inBindings) {
        this.myEnv = (inEnv != null)
                ? (Hashtable) (inEnv.clone())
                : new Hashtable(3, 0.75f);

        if (inBindings != null) {
            final Map<String, Object> clonedBinding = new HashMap<String, Object>();
            clonedBinding.putAll(inBindings);
            this.bindings = clonedBinding;
        } else {
            this.bindings = Collections.synchronizedMap(new HashMap<String, Object>());
        }
    }

    private FlatCtx cloneCtx() {
        return new FlatCtx(myEnv, this.bindings);
    }

    /**
     * Utility method for processing composite/compound name.
     *
     * @param name The non-null composite or compound name to process.
     * @return The non-null string name in this name space to be processed.
     */
    protected String getMyComponents(Name name) throws NamingException {
        if (name instanceof CompositeName) {
            if (name.size() > 1) {
                throw new InvalidNameException(name.toString() + " has more components than namespace can handle");
            }
            return name.get(0);
        } else {
            // compound name
            return name.toString();
        }
    }

    @Override
    public Object lookup(String name) throws NamingException {
        return lookup(new CompositeName(name));
    }

    @Override
    public Object lookup(Name name) throws NamingException {
        if (name.isEmpty()) {
            // Asking to look up this context itself.  Create and return
            // a new instance with its own independent environment.
            return (cloneCtx());
        }

        // Extract components that belong to this namespace
        final String nm = getMyComponents(name);

        // Find object in internal hash table
        final Object answer = bindings.get(nm);
        if (answer == null) {
            throw new NameNotFoundException(name + " not found");
        }
        return answer;
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        bind(new CompositeName(name), obj);
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot bind empty name");
        }

        // Extract components that belong to this namespace
        String nm = getMyComponents(name);

        // Find object in internal hash table
        if (bindings.get(nm) != null) {
            throw new NameAlreadyBoundException("Use rebind to override");
        }

        // Add object to internal hash table
        bindings.put(nm, obj);
    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {
        rebind(new CompositeName(name), obj);
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot bind empty name");
        }

        // Extract components that belong to this namespace
        final String nm = getMyComponents(name);

        // Add object to internal hash table
        bindings.put(nm, obj);
    }

    @Override
    public void unbind(String name) throws NamingException {
        unbind(new CompositeName(name));
    }

    @Override
    public void unbind(Name name) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot unbind empty name");
        }

        // Extract components that belong to this namespace
        String nm = getMyComponents(name);

        // Remove object from internal hash table
        bindings.remove(nm);
    }

    @Override
    public void rename(String oldname, String newname) throws NamingException {
        rename(new CompositeName(oldname), new CompositeName(newname));
    }

    @Override
    public void rename(Name oldname, Name newname) throws NamingException {
        if (oldname.isEmpty() || newname.isEmpty()) {
            throw new InvalidNameException("Cannot rename empty name");
        }

        // Extract components that belong to this namespace
        final String oldnm = getMyComponents(oldname);
        final String newnm = getMyComponents(newname);

        // Check if new name exists
        if (bindings.get(newnm) != null) {
            throw new NameAlreadyBoundException(newname.toString() + " is already bound");
        }

        // Check if old name is bound
        Object oldBinding = bindings.remove(oldnm);
        if (oldBinding == null) {
            throw new NameNotFoundException(oldname.toString() + " not bound");
        }

        bindings.put(newnm, oldBinding);
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return list(new CompositeName(name));
    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        if (name.isEmpty()) {
            // listing this context
            return new ListOfNames2(bindings.keySet());
        }

        // Perhaps 'name' names a context
        Object target = lookup(name);
        if (target instanceof Context) {
            try {
                return ((Context) target).list("");
            } finally {
                ((Context) target).close();
            }
        }
        throw new NotContextException(name + " cannot be listed");
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        return listBindings(new CompositeName(name));
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        if (name.isEmpty()) {
            // listing this context
            return new ListOfBindings2(bindings.keySet());
        }

        // Perhaps 'name' names a context
        Object target = lookup(name);
        if (target instanceof Context) {
            try {
                return ((Context) target).listBindings("");
            } finally {
                ((Context) target).close();
            }
        }
        throw new NotContextException(name + " cannot be listed");
    }

    @Override
    public void destroySubcontext(String name) throws NamingException {
        destroySubcontext(new CompositeName(name));
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
        throw new OperationNotSupportedException("FlatCtx does not support subcontexts");
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return createSubcontext(new CompositeName(name));
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        throw new OperationNotSupportedException("FlatCtx does not support subcontexts");
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return lookupLink(new CompositeName(name));
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        // This flat context does not treat links specially
        return lookup(name);
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        return getNameParser(new CompositeName(name));
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        // Do lookup to verify name exists
        Object obj = lookup(name);
        if (obj instanceof Context) {
            ((Context) obj).close();
        }
        return myParser;
    }

    @Override
    public String composeName(String name, String prefix)
            throws NamingException {
        Name result = composeName(new CompositeName(name),
                new CompositeName(prefix));
        return result.toString();
    }

    @Override
    public Name composeName(Name name, Name prefix)
            throws NamingException {
        Name result = (Name) (prefix.clone());
        result.addAll(name);
        return result;
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal)
            throws NamingException {
//        if (myEnv == null) {
//            myEnv = new Hashtable(5, 0.75f);
//        }
        return myEnv.put(propName, propVal);
    }

    @Override
    public Object removeFromEnvironment(String propName)
            throws NamingException {
        return myEnv.remove(propName);
    }

    @Override
    public Hashtable getEnvironment() throws NamingException {
        return (Hashtable) myEnv.clone();
    }

    @Override
    public String getNameInNamespace() throws NamingException {
        return "";
    }

    @Override
    public void close() throws NamingException {
    }

    //---
    // Class for enumerating name/class pairs
    class ListOfNames2 implements NamingEnumeration<NameClassPair> {

        private Iterator<String> names;

        ListOfNames2(Set<String> names) {
            this.names = names.iterator();
        }

        @Override
        public boolean hasMoreElements() {
            try {
                return hasMore();
            } catch (NamingException e) {
                return false;
            }
        }

        @Override
        public boolean hasMore() throws NamingException {
            return names.hasNext();
        }

        @Override
        public NameClassPair nextElement() {
            try {
                return next();
            } catch (NamingException e) {
                throw new NoSuchElementException(e.toString());
            }
        }

        @Override
        public NameClassPair next() throws NamingException {
            String name = (String) names.next();
            String className = bindings.get(name).getClass().getName();
            return new NameClassPair(name, className);
        }

        @Override
        public void close() {
        }
    }

    // Class for enumerating bindings
    class ListOfBindings2 implements NamingEnumeration<Binding> {

        private Iterator<String> names;

        ListOfBindings2(Set<String> names) {
            this.names = names.iterator();
        }

        @Override
        public boolean hasMoreElements() {
            try {
                return hasMore();
            } catch (NamingException e) {
                return false;
            }
        }

        @Override
        public boolean hasMore() throws NamingException {
            return names.hasNext();
        }

        @Override
        public Binding nextElement() {
            try {
                return next();
            } catch (NamingException e) {
                throw new NoSuchElementException(e.toString());
            }
        }

        @Override
        public Binding next() throws NamingException {
            String name = (String) names.next();
            return new Binding(name, bindings.get(name));
        }

        @Override
        public void close() {
        }
    }
};
