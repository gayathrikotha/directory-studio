/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

package org.apache.directory.ldapstudio.browser.common.widgets.connection;


import org.apache.directory.ldapstudio.browser.common.BrowserCommonActivator;
import org.apache.directory.ldapstudio.browser.common.BrowserCommonConstants;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


/**
 * The ConnectionLabelProvider represents the label provider for
 * the connection widget.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ConnectionLabelProvider extends LabelProvider
{

    /**
     * {@inheritDoc}
     * 
     * This implementation returns the connection name and appends information
     * about the used encryption method.
     */
    public String getText( Object obj )
    {
        if ( obj instanceof IConnection )
        {
            IConnection conn = ( IConnection ) obj;
            if ( conn.getEncryptionMethod() == IConnection.ENCYRPTION_LDAPS )
            {
                return conn.getName() + " (LDAPS)";
            }
            else if ( conn.getEncryptionMethod() == IConnection.ENCYRPTION_STARTTLS )
            {
                return conn.getName() + " (StartTLS)";
            }
            else
            {
                return conn.getName();
            }
        }
        else if ( obj != null )
        {
            return obj.toString();
        }
        else
        {
            return "";
        }
    }


    /**
     * {@inheritDoc}
     * 
     * This implementation returns a icon for connected or disconnected state.
     */
    public Image getImage( Object obj )
    {
        if ( obj instanceof IConnection )
        {
            IConnection conn = ( IConnection ) obj;
            return conn.isOpened() ? BrowserCommonActivator.getDefault().getImage( BrowserCommonConstants.IMG_CONNECTION_CONNECTED )
                : BrowserCommonActivator.getDefault().getImage( BrowserCommonConstants.IMG_CONNECTION_DISCONNECTED );
        }
        else
        {
            return null;
        }
    }

}