/**
 * Copyright (C) 2011 University Lille 1, Inria
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301, USA.
 *
 * Contact: romain.rouvoy@univ-lille1.fr
 */
package container.fractal.applications;

import static org.objectweb.fractal.util.Fractal.getBindingController;
import static org.objectweb.fractal.util.Fractal.getContentController;
import static org.objectweb.fractal.util.Fractal.getGenericFactory;
import static org.objectweb.fractal.util.Fractal.getNameController;
import static org.objectweb.fractal.util.Fractal.getTypeFactory;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;

import container.java.applications.api.IFibonacci;

/**
 * Component-based implementation of the Fibonacci algorithm.
 * 
 * @author <a href="mailto:Romain.Rouvoy@lifl.fr">Romain Rouvoy</a>
 */
public enum FibonacciType {
    FIBONACCI {
        @Override
        public ComponentType createType(Component boot) throws Exception {
            TypeFactory tf = getTypeFactory(boot);
            return tf.createFcType(new InterfaceType[] { tf.createFcItfType(
                    "fibonacci", IFibonacci.class.getName(), false, false,
                    false) });
        }
    },
    FIBONACCI_DELEGATE {
        @Override
        public ComponentType createType(Component boot) throws Exception {
            TypeFactory tf = getTypeFactory(boot);
            return tf.createFcType(new InterfaceType[] {
                    tf.createFcItfType("fibonacci", IFibonacci.class.getName(),
                            false, false, false),
                    tf.createFcItfType("delegate", IFibonacci.class.getName(),
                            true, false, false) });
        }
    };

    public abstract ComponentType createType(Component boot) throws Exception;

    public Component createPrimitive(Component boot,
            Class<? extends IFibonacci> content) throws Exception {
        GenericFactory cf = getGenericFactory(boot);
        Component comp = cf.newFcInstance(createType(boot), "primitive",
                content.getName());
        getNameController(comp).setFcName("fibonacci-primitive");
        return comp;
    }

    public Component createComposite(Component boot, Component content)
            throws Exception {
        GenericFactory cf = getGenericFactory(boot);
        Component rComp = cf.newFcInstance(createType(boot), "composite", null);
        getNameController(rComp).setFcName("fibonacci-composite");
        getContentController(rComp).addFcSubComponent(content);
        getBindingController(rComp).bindFc("fibonacci", getInterface(content));
        return rComp;
    }

    public IFibonacci getInterface(Component c) {
        try {
            return (IFibonacci) c.getFcInterface("fibonacci");
        } catch (NoSuchInterfaceException e) {
            return null;
        }
    }
}
