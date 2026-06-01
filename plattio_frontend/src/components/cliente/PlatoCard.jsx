import React from 'react';
import '../../styles/PlatoCard.css';
import agregarIcon from '../../assets/icons/sumar.png';
import quitarIcon from '../../assets/icons/restar.png';

const PlatoCard = ({ plato, cantidad, onAgregar, onQuitar }) => {
    return (
        <div className="plato-card">
            <div className="plato-info">
                <div className="plato-nombre">{plato.nombre}</div>
                <div className="plato-detalle">{plato.descripcion}</div>
            </div>

            <div className="plato-precio">${plato.precio.toLocaleString('es-AR')}</div>

            <div className="plato-controles">
                {cantidad > 0 && (
                    <>
                        <img
                            src={quitarIcon}
                            alt="Quitar"
                            className="icono-control"
                            onClick={onQuitar}
                        />
                        <span className="cantidad">{cantidad}</span>
                    </>
                )}
                <img
                    src={agregarIcon}
                    alt="Agregar"
                    className="icono-control"
                    onClick={onAgregar}
                />
            </div>

        </div>
    );
};

export default PlatoCard;
