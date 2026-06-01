// src/components/PedidoColumn.js
import React from 'react';
import PedidoCard from './PedidoCard';

const PedidoColumn = ({ titulo, pedidos, onMarcarPlato, onAbrirModal, abrirModalNotas, onCambiarEstado }) => {
    return (
        <div style={{ flex: 1 }}>
            <div>
                <h2>{titulo}</h2>
            </div>
            <div className="pedido-columna">
                {pedidos.map((pedido) => (
                    <PedidoCard
                        key={pedido.id}
                        pedido={pedido}
                        onMarcarPlato={onMarcarPlato}
                        onAbrirModal={onAbrirModal}
                        abrirModalNotas={abrirModalNotas}
                        onCambiarEstado={onCambiarEstado}
                    />
                ))}
            </div>
        </div>
    );
};

export default PedidoColumn;
