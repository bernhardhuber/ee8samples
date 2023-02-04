/* 
 * "@(#)HierCtx.java	1.1	00/01/18 SMI"
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
package org.huberb.ee8sample.jndi.impl.hierarchysearch;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
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
import javax.naming.spi.NamingManager;

/**
 * A sample service provider that implements a hierarchical name space in memory.
 */
public class HierCtx implements Context {

    protected final static NameParser myParser = new HierParser();

    protected Hashtable myEnv;
    protected Hashtable bindings = new Hashtable(11);
    protected HierCtx parent = null;
    protected String myAtomicName = null;

    HierCtx(Hashtable inEnv) {
        myEnv = (inEnv != null)
                ? (Hashtable) (inEnv.clone())
                : null;
    }

    protected HierCtx(HierCtx parent, String name, Hashtable inEnv,
            Hashtable bindings) {
        this(inEnv);
        this.parent = parent;
        myAtomicName = name;
        this.bindings = (Hashtable) bindings.clone();
    }

    protected Context createCtx(HierCtx parent, String name, Hashtable inEnv) {
        return new HierCtx(parent, name, inEnv, new Hashtable(11));
    }

    protected Context cloneCtx() {
        return new HierCtx(parent, myAtomicName, myEnv, bindings);
    }

    /**
     * Utility method for processing composite/compound name.
     *
     * @param name The non-null composite or compound name to process.
     * @return The non-null string name in this name space to be processed.
     * @throws javax.naming.NamingException
     */
    protected Name getMyComponents(Name name) throws NamingException {
        if (name instanceof CompositeName) {
            if (name.size() > 1) {
                throw new InvalidNameException(name.toString()
                        + " has more components than namespace can handle");
            }

            // Turn component that belongs to us into compound name
            return myParser.parse(name.get(0));
        } else {
            // Already parsed
            return name;
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
            return cloneCtx();
        }

        // Extract components that belong to this namespace
        Name nm = getMyComponents(name);
        String atom = nm.get(0);
        Object inter = bindings.get(atom);

        if (nm.size() == 1) {
            // Atomic name: Find object in internal data structure
            if (inter == null) {
                throw new NameNotFoundException(name + " not found");
            }

            // Call getObjectInstance for using any object factories
            try {
                return NamingManager.getObjectInstance(inter,
                        new CompositeName().add(atom),
                        this, myEnv);
            } catch (Exception e) {
                NamingException ne = new NamingException("getObjectInstance failed");
                ne.setRootCause(e);
                throw ne;
            }
        } else {
            // Intermediate name: Consume name in this context and continue
            if (!(inter instanceof Context)) {
                throw new NotContextException(atom + " does not name a context");
            }

            return ((Context) inter).lookup(nm.getSuffix(1));
        }
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
        Name nm = getMyComponents(name);
        String atom = nm.get(0);
        Object inter = bindings.get(atom);

        if (nm.size() == 1) {
            // Atomic name: Find object in internal data structure
            if (inter != null) {
                throw new NameAlreadyBoundException("Use rebind to override");
            }

            // Call getStateToBind for using any state factories
            obj = NamingManager.getStateToBind(obj,
                    new CompositeName().add(atom),
                    this, myEnv);

            // Add object to internal data structure
            bindings.put(atom, obj);
        } else {
            // Intermediate name: Consume name in this context and continue
            if (!(inter instanceof Context)) {
                throw new NotContextException(atom + " does not name a context");
            }
            ((Context) inter).bind(nm.getSuffix(1), obj);
        }
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
        Name nm = getMyComponents(name);
        String atom = nm.get(0);

        if (nm.size() == 1) {
            // Atomic name

            // Call getStateToBind for using any state factories
            obj = NamingManager.getStateToBind(obj,
                    new CompositeName().add(atom),
                    this, myEnv);

            // Add object to internal data structure
            bindings.put(atom, obj);
        } else {
            // Intermediate name: Consume name in this context and continue
            Object inter = bindings.get(atom);
            if (!(inter instanceof Context)) {
                throw new NotContextException(atom + " does not name a context");
            }
            ((Context) inter).rebind(nm.getSuffix(1), obj);
        }
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
        Name nm = getMyComponents(name);
        String atom = nm.get(0);

        // Remove object from internal data structure
        if (nm.size() == 1) {
            // Atomic name: Find object in internal data structure
            bindings.remove(atom);
        } else {
            // Intermediate name: Consume name in this context and continue
            Object inter = bindings.get(atom);
            if (!(inter instanceof Context)) {
                throw new NotContextException(atom + " does not name a context");
            }
            ((Context) inter).unbind(nm.getSuffix(1));
        }
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
        Name oldnm = getMyComponents(oldname);
        Name newnm = getMyComponents(newname);

        // Simplistic implementation: support only rename within same context
        if (oldnm.size() != newnm.size()) {
            throw new OperationNotSupportedException("Do not support rename across different contexts");
        }

        String oldatom = oldnm.get(0);
        String newatom = newnm.get(0);

        if (oldnm.size() == 1) {
            // Atomic name: Add object to internal data structure
            // Check if new name exists
            if (bindings.get(newatom) != null) {
                throw new NameAlreadyBoundException(newname.toString() + " is already bound");
            }

            // Check if old name is bound
            Object oldBinding = bindings.remove(oldatom);
            if (oldBinding == null) {
                throw new NameNotFoundException(oldname.toString() + " not bound");
            }

            bindings.put(newatom, oldBinding);
        } else {
            // Simplistic implementation: support only rename within same context
            if (!oldatom.equals(newatom)) {
                throw new OperationNotSupportedException("Do not support rename across different contexts");
            }

            // Intermediate name: Consume name in this context and continue
            Object inter = bindings.get(oldatom);
            if (!(inter instanceof Context)) {
                throw new NotContextException(oldatom + " does not name a context");
            }
            ((Context) inter).rename(oldnm.getSuffix(1), newnm.getSuffix(1));
        }
    }

    @Override
    public NamingEnumeration list(String name) throws NamingException {
        return list(new CompositeName(name));
    }

    @Override
    public NamingEnumeration list(Name name) throws NamingException {
        if (name.isEmpty()) {
            // listing this context
            return new ListOfNames(bindings.keys());
        }

        // Perhaps 'name' names a context
        Object target = lookup(name);
        if (target instanceof Context) {
            return ((Context) target).list("");
        }
        throw new NotContextException(name + " cannot be listed");
    }

    @Override
    public NamingEnumeration listBindings(String name) throws NamingException {
        return listBindings(new CompositeName(name));
    }

    @Override
    public NamingEnumeration listBindings(Name name) throws NamingException {
        if (name.isEmpty()) {
            // listing this context
            return new ListOfBindings(bindings.keys());
        }

        // Perhaps 'name' names a context
        Object target = lookup(name);
        if (target instanceof Context) {
            return ((Context) target).listBindings("");
        }
        throw new NotContextException(name + " cannot be listed");
    }

    @Override
    public void destroySubcontext(String name) throws NamingException {
        destroySubcontext(new CompositeName(name));
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot destroy context using empty name");
        }

        // Simplistic implementation: not checking for nonempty context first
        // Use same implementation as unbind
        unbind(name);
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return createSubcontext(new CompositeName(name));
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException("Cannot bind empty name");
        }

        // Extract components that belong to this namespace
        Name nm = getMyComponents(name);
        String atom = nm.get(0);
        Object inter = bindings.get(atom);

        if (nm.size() == 1) {
            // Atomic name: Find object in internal data structure
            if (inter != null) {
                throw new NameAlreadyBoundException("Use rebind to override");
            }

            // Create child
            Context child = createCtx(this, atom, myEnv);

            // Add child to internal data structure
            bindings.put(atom, child);

            return child;
        } else {
            // Intermediate name: Consume name in this context and continue
            if (!(inter instanceof Context)) {
                throw new NotContextException(atom + " does not name a context");
            }
            return ((Context) inter).createSubcontext(nm.getSuffix(1));
        }
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return lookupLink(new CompositeName(name));
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
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
    public Name composeName(Name name, Name prefix) throws NamingException {
        Name result;

        // Both are compound names, compose using compound name rules
        if (!(name instanceof CompositeName)
                && !(prefix instanceof CompositeName)) {
            result = (Name) (prefix.clone());
            result.addAll(name);
            return new CompositeName().add(result.toString());
        }

        // Simplistic implementation: do not support federation
        throw new OperationNotSupportedException("Do not support composing composite names");
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal)
            throws NamingException {
        if (myEnv == null) {
            myEnv = new Hashtable(5, 0.75f);
        }
        return myEnv.put(propName, propVal);
    }

    @Override
    public Object removeFromEnvironment(String propName)
            throws NamingException {
        if (myEnv == null) {
            return null;
        }

        return myEnv.remove(propName);
    }

    @Override
    public Hashtable getEnvironment() throws NamingException {
        if (myEnv == null) {
            // Must return non-null
            return new Hashtable(3, 0.75f);
        } else {
            return (Hashtable) myEnv.clone();
        }
    }

    @Override
    public String getNameInNamespace() throws NamingException {
        HierCtx ancestor = parent;

        // No ancestor
        if (ancestor == null) {
            return "";
        }

        Name name = myParser.parse("");
        name.add(myAtomicName);

        // Get parent's names
        while (ancestor != null && ancestor.myAtomicName != null) {
            name.add(0, ancestor.myAtomicName);
            ancestor = ancestor.parent;
        }

        return name.toString();
    }

    @Override
    public String toString() {
        if (myAtomicName != null) {
            return myAtomicName;
        } else {
            return "ROOT CONTEXT";
        }
    }

    @Override
    public void close() throws NamingException {
    }

    // Class for enumerating name/class pairs
    class ListOfNames implements NamingEnumeration {

        protected Enumeration names;

        ListOfNames(Enumeration names) {
            this.names = names;
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
            return names.hasMoreElements();
        }

        @Override
        public Object next() throws NamingException {
            String name = (String) names.nextElement();
            String className = bindings.get(name).getClass().getName();
            return new NameClassPair(name, className);
        }

        @Override
        public Object nextElement() {
            try {
                return next();
            } catch (NamingException e) {
                throw new NoSuchElementException(e.toString());
            }
        }

        @Override
        public void close() {
        }
    }

    // Class for enumerating bindings
    class ListOfBindings extends ListOfNames {

        ListOfBindings(Enumeration names) {
            super(names);
        }

        @Override
        public Object next() throws NamingException {
            String name = (String) names.nextElement();
            Object obj = bindings.get(name);

            try {
                obj = NamingManager.getObjectInstance(obj,
                        new CompositeName().add(name), HierCtx.this,
                        HierCtx.this.myEnv);
            } catch (Exception e) {
                NamingException ne = new NamingException(
                        "getObjectInstance failed");
                ne.setRootCause(e);
                throw ne;
            }

            return new Binding(name, obj);
        }
    }

};
