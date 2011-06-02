/*
 * Copyright (c) 2011, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.view;

import com.dooapp.fxform.model.EnumProperty;
import com.dooapp.fxform.model.FormField;
import com.dooapp.fxform.model.FormFieldController;
import com.dooapp.fxform.view.delegate.*;
import com.dooapp.fxform.view.handler.FieldHandler;
import com.dooapp.fxform.view.handler.TypeFieldHandler;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Antoine Mischler
 * Date: 11/04/11
 * Time: 22:59
 * <p/>
 * Factory implementation based on delegates.
 */
public class DelegateFactoryImpl implements EditorFactory {

    private final static EditorFactory DEFAULT_FACTORY = new EditorFactory() {

        public Node createNode(FormFieldController formFieldController) throws NodeCreationException {
            return new Label(formFieldController.getFormField().getField().getType() + " not supported");
        }
    };

    private Map<FieldHandler, EditorFactory> map = new HashMap();

    public DelegateFactoryImpl() {
        // register default delegates
        map.put(new TypeFieldHandler(StringProperty.class), new StringPropertyDelegate());
        map.put(new TypeFieldHandler(BooleanProperty.class), new BooleanPropertyDelegate());
        map.put(new TypeFieldHandler(EnumProperty.class), new EnumPropertyDelegate());
        map.put(new TypeFieldHandler(IntegerProperty.class), new IntegerPropertyDelegate());
        map.put(new TypeFieldHandler(LongProperty.class), new LongPropertyDelegate());
        map.put(new TypeFieldHandler(DoubleProperty.class), new DoublePropertyDelegate());
    }

    public Node createNode(FormFieldController controller) throws NodeCreationException {
        EditorFactory delegate = getDelegate(controller.getFormField());
        return delegate.createNode(controller);
    }

    private EditorFactory getDelegate(FormField formField) {
        for (FieldHandler handler : map.keySet()) {
            if (handler.handle(formField.getField())) {
                return map.get(handler);
            }
        }
        return DEFAULT_FACTORY;
    }
}
