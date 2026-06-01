import React, { useState } from 'react';
import '../styles/OrdenamientoBar.css';
import cerebroIcon from '../assets/icons/cerebro.png';
import timerIcon from '../assets/icons/timer.png';
import personalizadoIcon from '../assets/icons/personalizado.png';
import llegadaIcon from '../assets/icons/llegada.png';
import editarIcon from '../assets/icons/edittt.png';
import tableroIcon from '../assets/icons/tablero.png';
import { useNavigate } from 'react-router-dom';


const OrdenamientoBar = ({ ordenarPor }) => {
    // Estados locales para saber qué botón está seleccionado en cada grupo
    const [selectedPendientes, setSelectedPendientes] = useState('inteligente');
    const [selectedProceso, setSelectedProceso] = useState('llegada-proceso');

    const [menuVisible, setMenuVisible] = useState(false);

    const navigate = useNavigate();


    const toggleMenu = () => {
        setMenuVisible(prev => !prev);
    };


    // Manejador que cambia el estado interno y llama al prop ordenarPor
    const handleClick = (valor) => {
        if (valor.endsWith('pendientes') || valor === 'inteligente' || valor === 'personalizado') {
            setSelectedPendientes(valor);
        } else if (valor.endsWith('proceso')) {
            setSelectedProceso(valor);
        }
        ordenarPor(valor);
    };

    return (
        <div className="ordenamiento-bar-container">
            <div className="ordenamiento-grupo izquierda">
                <div className="menu-container">
                    <button className="menu-btn" onClick={toggleMenu}>☰</button>
                    {menuVisible && (
                        <div className="menu-dropdown">
                            <div onClick={() => navigate('/cocina')}>
                                <img src={tableroIcon} alt="Tablero" className="menu-icon" />
                                Ver tablero de comandas
                            </div>
                            <hr className="menu-divider" />
                            <div onClick={() => navigate('/editarcarta?origen=cocina')}>
                                <img src={editarIcon} alt="Editar" className="menu-icon" />
                                Editar carta
                            </div>
                        </div>

                    )}
                </div>


                <button
                    className={`${selectedPendientes === 'inteligente-pendientes' ? 'selected' : ''} boton-inteligente-recomendado`}
                    onClick={() => handleClick('inteligente-pendientes')}
                >
                    <img src={cerebroIcon} alt="Inteligente" style={{ width: 16, height: 16, marginRight: 5 }} /> Inteligente
                </button>

                <button
                    className={selectedPendientes === 'llegada-pendientes' ? 'selected' : ''}
                    onClick={() => handleClick('llegada-pendientes')}
                >
                    <img src={llegadaIcon} alt="Por llegada" style={{ width: 16, height: 16, marginRight: 5 }} /> Por llegada
                </button>

                <button
                    className={selectedPendientes === 'timer-pendientes' ? 'selected' : ''}
                    onClick={() => handleClick('timer-pendientes')}
                >
                    <img src={timerIcon} alt="Por timer" style={{ width: 16, height: 16, marginRight: 5 }} /> Por timer
                </button>

                <button
                    className={selectedPendientes === 'personalizado' ? 'selected' : ''}
                    onClick={() => handleClick('personalizado')}
                >
                    <img src={personalizadoIcon} alt="Personalizado" style={{ width: 16, height: 16, marginRight: 5 }} /> Personalizado
                </button>
            </div>

            <div className="ordenamiento-grupo derecha">
                <button
                    className={selectedProceso === 'llegada-proceso' ? 'selected' : ''}
                    onClick={() => handleClick('llegada-proceso')}
                >
                    <img src={llegadaIcon} alt="Por llegada" style={{ width: 16, height: 16, marginRight: 5 }} /> Por llegada
                </button>

                <button
                    className={selectedProceso === 'timer-proceso' ? 'selected' : ''}
                    onClick={() => handleClick('timer-proceso')}
                >
                    <img src={timerIcon} alt="Por timer" style={{ width: 16, height: 16, marginRight: 5 }} /> Por timer
                </button>
            </div>
        </div>
    );
};

export default OrdenamientoBar;