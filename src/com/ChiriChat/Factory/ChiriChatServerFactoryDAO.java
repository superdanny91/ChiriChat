package com.ChiriChat.Factory;

import com.ChiriChat.DataAccessObject.InterfacesDAO.IContactosDAO;
import com.ChiriChat.DataAccessObject.InterfacesDAO.IConversacioneDAO;
import com.ChiriChat.DataAccessObject.InterfacesDAO.IMensajesDAO;

/**
 * Created by danny on 27/05/14.
 */
public class ChiriChatServerFactoryDAO implements FactoryDAO {
    @Override
    public IMensajesDAO getMensajesDAO() {
        return null;
    }

    @Override
    public IConversacioneDAO getConversacionesDAO() {
        return null;
    }

    @Override
    public IContactosDAO getContactosDAO() {
        return null;
    }
}