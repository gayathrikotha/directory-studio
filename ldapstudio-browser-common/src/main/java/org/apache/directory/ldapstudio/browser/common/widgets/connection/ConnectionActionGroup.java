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


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.directory.ldapstudio.browser.common.actions.CloseConnectionAction;
import org.apache.directory.ldapstudio.browser.common.actions.CopyAction;
import org.apache.directory.ldapstudio.browser.common.actions.DeleteAction;
import org.apache.directory.ldapstudio.browser.common.actions.NewConnectionAction;
import org.apache.directory.ldapstudio.browser.common.actions.OpenConnectionAction;
import org.apache.directory.ldapstudio.browser.common.actions.PasteAction;
import org.apache.directory.ldapstudio.browser.common.actions.PropertiesAction;
import org.apache.directory.ldapstudio.browser.common.actions.RenameAction;
import org.apache.directory.ldapstudio.browser.common.actions.proxy.BrowserActionProxy;
import org.apache.directory.ldapstudio.browser.common.actions.proxy.ConnectionViewActionProxy;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ICommandService;


/**
 * This class manages all the actions of the connection widget.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ConnectionActionGroup implements IMenuListener
{

    /** The Constant newConnectionAction. */
    protected static final String newConnectionAction = "newConnectionAction";

    /** The Constant openConnectionAction. */
    protected static final String openConnectionAction = "openConnectionAction";

    /** The Constant closeConnectionAction. */
    protected static final String closeConnectionAction = "closeConnectionAction";

    /** The Constant copyConnectionAction. */
    protected static final String copyConnectionAction = "copyConnectionAction";

    /** The Constant pasteConnectionAction. */
    protected static final String pasteConnectionAction = "pasteConnectionAction";

    /** The Constant deleteConnectionAction. */
    protected static final String deleteConnectionAction = "deleteConnectionAction";

    /** The Constant renameConnectionAction. */
    protected static final String renameConnectionAction = "renameConnectionAction";

    /** The Constant propertyDialogAction. */
    protected static final String propertyDialogAction = "propertyDialogAction";

    /** The action map. */
    protected Map<String, ConnectionViewActionProxy> connectionActionMap;

    /** The action bars. */
    protected IActionBars actionBars;

    /** The connection main widget. */
    protected ConnectionWidget mainWidget;


    /**
     * Creates a new instance of ConnectionActionGroup.
     *
     * @param mainWidget the connection main widget
     * @param configuration the connection widget configuration
     */
    public ConnectionActionGroup( ConnectionWidget mainWidget, ConnectionConfiguration configuration )
    {
        this.mainWidget = mainWidget;
        this.connectionActionMap = new HashMap<String, ConnectionViewActionProxy>();

        TableViewer viewer = mainWidget.getViewer();
        connectionActionMap
            .put( newConnectionAction, new ConnectionViewActionProxy( viewer, new NewConnectionAction() ) );
        connectionActionMap.put( openConnectionAction, new ConnectionViewActionProxy( viewer,
            new OpenConnectionAction() ) );
        connectionActionMap.put( closeConnectionAction, new ConnectionViewActionProxy( viewer,
            new CloseConnectionAction() ) );
        connectionActionMap.put( pasteConnectionAction, new ConnectionViewActionProxy( viewer, new PasteAction() ) );
        connectionActionMap.put( copyConnectionAction, new ConnectionViewActionProxy( viewer, new CopyAction(
            ( BrowserActionProxy ) connectionActionMap.get( pasteConnectionAction ) ) ) );
        connectionActionMap.put( deleteConnectionAction, new ConnectionViewActionProxy( viewer, new DeleteAction() ) );
        connectionActionMap.put( renameConnectionAction, new ConnectionViewActionProxy( viewer, new RenameAction() ) );
        connectionActionMap.put( propertyDialogAction, new ConnectionViewActionProxy( viewer, new PropertiesAction() ) );
    }


    /**
     * Disposes this action group.
     */
    public void dispose()
    {
        if ( mainWidget != null )
        {
            for ( Iterator it = connectionActionMap.keySet().iterator(); it.hasNext(); )
            {
                String key = ( String ) it.next();
                ConnectionViewActionProxy action = ( ConnectionViewActionProxy ) this.connectionActionMap.get( key );
                action.dispose();
                action = null;
                it.remove();
            }
            connectionActionMap.clear();
            connectionActionMap = null;

            actionBars = null;
            mainWidget = null;
        }
    }


    /**
     * Enables the action handlers.
     *
     * @param actionBars the action bars
     */
    public void enableGlobalActionHandlers( IActionBars actionBars )
    {
        this.actionBars = actionBars;
        activateGlobalActionHandlers();
    }


    /**
     * Fills the tool bar.
     *
     * @param toolBarManager the tool bar manager
     */
    public void fillToolBar( IToolBarManager toolBarManager )
    {
        toolBarManager.add( ( IAction ) this.connectionActionMap.get( newConnectionAction ) );
        toolBarManager.add( new Separator() );
        toolBarManager.add( ( IAction ) this.connectionActionMap.get( openConnectionAction ) );
        toolBarManager.add( ( IAction ) this.connectionActionMap.get( closeConnectionAction ) );

        toolBarManager.update( true );
    }


    /**
     * Fills the local menu.
     *
     * @param menuManager the local menu manager
     */
    public void fillMenu( IMenuManager menuManager )
    {
        // menuManager.add(this.openSortDialogAction);
        // menuManager.add(new Separator());
        // menuManager.update(true);
    }


    /**
     * Fills the context menu.
     *
     * @param menuManager the context menu manager
     */
    public void fillContextMenu( IMenuManager menuManager )
    {
        menuManager.setRemoveAllWhenShown( true );
        menuManager.addMenuListener( this );
    }


    /**
     * {@inheritDoc}
     * 
     * This implementation fills the context menu.
     */
    public void menuAboutToShow( IMenuManager menuManager )
    {
        // add
        menuManager.add( ( IAction ) connectionActionMap.get( newConnectionAction ) );
        menuManager.add( new Separator() );

        // open/close
        if ( ( ( IAction ) connectionActionMap.get( closeConnectionAction ) ).isEnabled() )
        {
            menuManager.add( ( IAction ) connectionActionMap.get( closeConnectionAction ) );
        }
        else if ( ( ( IAction ) connectionActionMap.get( openConnectionAction ) ).isEnabled() )
        {
            menuManager.add( ( IAction ) connectionActionMap.get( openConnectionAction ) );
        }
        menuManager.add( new Separator() );

        // copy/paste/...
        menuManager.add( ( IAction ) connectionActionMap.get( copyConnectionAction ) );
        menuManager.add( ( IAction ) connectionActionMap.get( pasteConnectionAction ) );
        menuManager.add( ( IAction ) connectionActionMap.get( deleteConnectionAction ) );
        menuManager.add( ( IAction ) connectionActionMap.get( renameConnectionAction ) );
        menuManager.add( new Separator() );

        // additions
        menuManager.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );

        // properties
        menuManager.add( ( IAction ) connectionActionMap.get( propertyDialogAction ) );
    }


    /**
     * Activates the action handlers.
     */
    public void activateGlobalActionHandlers()
    {
        ICommandService commandService = ( ICommandService ) PlatformUI.getWorkbench().getAdapter(
            ICommandService.class );

        if ( actionBars != null )
        {
            actionBars.setGlobalActionHandler( ActionFactory.COPY.getId(), ( IAction ) connectionActionMap
                .get( copyConnectionAction ) );
            actionBars.setGlobalActionHandler( ActionFactory.PASTE.getId(), ( IAction ) connectionActionMap
                .get( pasteConnectionAction ) );
            actionBars.setGlobalActionHandler( ActionFactory.DELETE.getId(), ( IAction ) connectionActionMap
                .get( deleteConnectionAction ) );
            actionBars.setGlobalActionHandler( ActionFactory.RENAME.getId(), ( IAction ) connectionActionMap
                .get( renameConnectionAction ) );
            actionBars.setGlobalActionHandler( ActionFactory.PROPERTIES.getId(), ( IAction ) connectionActionMap
                .get( propertyDialogAction ) );
            actionBars.updateActionBars();
        }
        else
        {
            if ( commandService != null )
            {
                IAction ca = ( IAction ) connectionActionMap.get( copyConnectionAction );
                ca.setActionDefinitionId( "org.apache.directory.ldapstudio.browser.action.copy" );
                commandService.getCommand( ca.getActionDefinitionId() ).setHandler( new ActionHandler( ca ) );

                IAction pa = ( IAction ) connectionActionMap.get( pasteConnectionAction );
                pa.setActionDefinitionId( "org.apache.directory.ldapstudio.browser.action.paste" );
                commandService.getCommand( pa.getActionDefinitionId() ).setHandler( new ActionHandler( pa ) );

                IAction da = ( IAction ) connectionActionMap.get( deleteConnectionAction );
                da.setActionDefinitionId( "org.apache.directory.ldapstudio.browser.action.delete" );
                commandService.getCommand( da.getActionDefinitionId() ).setHandler( new ActionHandler( da ) );

                IAction pda = ( IAction ) connectionActionMap.get( propertyDialogAction );
                pda.setActionDefinitionId( "org.apache.directory.ldapstudio.browser.action.properties" );
                commandService.getCommand( pda.getActionDefinitionId() ).setHandler( new ActionHandler( pda ) );

            }
        }
    }


    /**
     * Deactivates the action handlers.
     */
    public void deactivateGlobalActionHandlers()
    {
        ICommandService commandService = ( ICommandService ) PlatformUI.getWorkbench().getAdapter(
            ICommandService.class );

        if ( actionBars != null )
        {
            actionBars.setGlobalActionHandler( ActionFactory.COPY.getId(), null );
            actionBars.setGlobalActionHandler( ActionFactory.PASTE.getId(), null );
            actionBars.setGlobalActionHandler( ActionFactory.DELETE.getId(), null );
            actionBars.setGlobalActionHandler( ActionFactory.RENAME.getId(), null );
            actionBars.setGlobalActionHandler( ActionFactory.PROPERTIES.getId(), null );
            actionBars.updateActionBars();
        }
        else
        {
            if ( commandService != null )
            {
                IAction ca = ( IAction ) connectionActionMap.get( copyConnectionAction );
                commandService.getCommand( ca.getActionDefinitionId() ).setHandler( null );

                IAction pa = ( IAction ) connectionActionMap.get( pasteConnectionAction );
                commandService.getCommand( pa.getActionDefinitionId() ).setHandler( null );

                IAction da = ( IAction ) connectionActionMap.get( deleteConnectionAction );
                commandService.getCommand( da.getActionDefinitionId() ).setHandler( null );

                IAction pda = ( IAction ) connectionActionMap.get( propertyDialogAction );
                commandService.getCommand( pda.getActionDefinitionId() ).setHandler( null );

            }
        }
    }

}
