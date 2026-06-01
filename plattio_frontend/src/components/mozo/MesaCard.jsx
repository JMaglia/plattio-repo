import React from 'react';
import { useNavigate } from 'react-router-dom';
import PedidoResumenLinea from './PedidoResumenLinea';
import iconNotas from '../../assets/icons/notas.png';
import iconMesa from '../../assets/icons/mesa.png';
import '../../styles/MesaCard.css';

const MesaCard = ({ numero, pedidos, totalPedidos, totalProductos }) => {

    const navigate = useNavigate();

    return (
        <div className="mesa-card">
            <h3 className="mesa-titulo">Mesa {numero}</h3>

            <div className="mesa-pedidos-scroll">
                {pedidos.map((pedido, index) => (
                    <PedidoResumenLinea
                        key={index}
                        categoria={pedido.categoria}
                        descripcion={pedido.descripcion}
                    />
                ))}
            </div>

            <hr className="mesa-divider" />

            <div className="mesa-info-resumen">
                <span className="mesa-resumen-text">
                    {totalPedidos} Pedido{totalPedidos > 1 ? 's' : ''} – {totalProductos} Producto{totalProductos > 1 ? 's' : ''}
                </span>
            </div>

            <div className="mesa-footer">
                <img src={iconMesa} alt="Mesa" className="mesa--icon"  onClick={() => navigate(`/mesa/${numero}?mozo=true`)}/>
                <img src={iconNotas} alt="Notas" className="mesa--icon" />
            </div>
        </div>
    );
};

export default MesaCard;
