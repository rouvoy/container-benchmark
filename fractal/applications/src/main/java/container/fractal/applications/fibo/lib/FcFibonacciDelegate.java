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
package container.fractal.applications.fibo.lib;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

import container.java.applications.fibo.api.IFibonacci;
import container.java.applications.fibo.lib.FibonacciDelegate;

/**
 * Component-based implementation of the Fibonacci algorithm.
 * 
 * @author <a href="mailto:Romain.Rouvoy@lifl.fr">Romain Rouvoy</a>
 */
public class FcFibonacciDelegate extends FibonacciDelegate implements
        BindingController {
    private static final String DELEGATE = "delegate";

    private static final boolean isDelegate(String label) {
        return DELEGATE.equals(label);
    }

    @Override
    public String[] listFc() {
        return new String[] { DELEGATE };
    }

    @Override
    public Object lookupFc(String clientItfName)
            throws NoSuchInterfaceException {
        if (isDelegate(clientItfName))
            return getDelegate();
        throw new NoSuchInterfaceException(clientItfName
                + " client interface not found.");
    }

    @Override
    public void bindFc(String clientItfName, Object serverItf)
            throws NoSuchInterfaceException, IllegalBindingException,
            IllegalLifeCycleException {
        if (isDelegate(clientItfName))
            setDelegate((IFibonacci)serverItf);
        else
            throw new NoSuchInterfaceException(clientItfName
                    + " client interface not found.");
    }

    @Override
    public void unbindFc(String clientItfName) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        if (isDelegate(clientItfName))
            setDelegate(null);
        else
            throw new NoSuchInterfaceException(clientItfName
                    + " client interface not found.");
    }
}
