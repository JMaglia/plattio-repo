// src/components/BottomNavBar.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import iconEditarCarta from '../../assets/icons/editarCarta.png';
import iconMesas from '../../assets/icons/mesas.png';
import iconCampana from '../../assets/icons/notificaciones.png';
import iconPedidos from '../../assets/icons/pedidos2.png';
import '../../styles/MozoNavBar.css';

const MozoNavBar = ({ activo, idMozo }) => {
    const navigate = useNavigate();

    return (
        <div className="bottom-nav">
            <img
                src={iconMesas}
                alt="Mesas"
                className={activo === 'mesas' ? 'activo' : ''}
                onClick={() => navigate(`/mozo/${idMozo}`)}
            />
            <img
                src={iconPedidos}
                alt="Pedidos"
                className={activo === 'pedidos' ? 'activo' : ''}
                onClick={() => navigate(`/mozo/pedidos/${idMozo}`)}
            />
            <img
                src={iconCampana}
                alt="Notificaciones"
                className={activo === 'notificaciones' ? 'activo' : ''}
                onClick={() => navigate(`/notificaciones/${idMozo}`)}
            />
            <img
                src={iconEditarCarta}
                alt="Editar Carta"
                className={activo === 'editarCarta' ? 'activo' : ''}
                onClick={() => navigate(`/editarCarta`)}
            />
        </div>
    );
};

export default MozoNavBar;
