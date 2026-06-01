import React from 'react';
import '../../styles/PedidoResumenLinea.css';

const PedidoResumenLinea = ({ categoria, descripcion }) => {
    return (
        <div className="pedido-resumen-linea">
            <div className="categoria">{categoria}</div>
            <div className="descripcion">{descripcion}</div>
            <hr className="pedido-divider" />
        </div>
    );
};

export default PedidoResumenLinea;
