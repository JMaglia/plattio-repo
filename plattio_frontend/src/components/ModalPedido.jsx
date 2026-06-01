import React from 'react';
import '../styles/ModalPedido.css';
import cerrarIcon from '../assets/icons/cerrar.png';
import notasIcon from '../assets/icons/notas.png';
import mesaIcon from '../assets/icons/mesa.png';
import deliveryIcon from '../assets/icons/delivery.png';

const PedidoModal = ({ pedido, onClose, onMarcarPlato, onCambiarEstado, abrirModalNotas }) => {
    if (!pedido) return null;

    const handleEstadoClick = () => {
        const nuevoEstado = pedido.estado === "Comenzar" ? "en_preparacion" : "pendiente";
        onCambiarEstado(pedido.id, nuevoEstado);
    };

    return (
        <div className="modal-backdrop">
            <div className="modal-detalle">
                <div className="pedido-header">
                    <div className="header-left">
                        <div className="icon-pointer" onClick={onClose}>
                            <img src={cerrarIcon} alt="Cerrar" style={{ width: 14, height: 14 }} />
                        </div>
                        <div className="mesa-numero">
                            {pedido.mesa === 0 ? (
                                <img src={deliveryIcon} alt="Delivery" style={{ width: 18, height: 18 }} />
                            ) : (
                                pedido.mesa
                            )}
                        </div>
                    </div>
                    <div className="header-center">
                        {/*<button className="estado-boton-modal" onClick={handleEstadoClick}>*/}
                        {/*    {pedido.estado}*/}
                        {/*</button>*/}
                    </div>
                    <div className="header-right">
                        <div className="hora-pedido">{pedido.tiempo}</div>
                    </div>
                </div>

                <div className="pedido-items-scroll">
                    {pedido.items.map((item, i) => (
                        <div className="plato-item" key={i}>
                            <div className="plato-info">
                                <div className="plato-nombre">{item.nombre}</div>
                                <div className="plato-detalle">{item.detalle}</div>
                                <div className="plato-detalle">{item.nota}</div>
                            </div>
                            <div className="plato-tiempo">x{item.cantidad}</div>
                            <input
                                type="checkbox"
                                checked={item.finalizado}
                                onChange={() => onMarcarPlato(pedido.id, item.id)}
                            />
                        </div>
                    ))}
                </div>

                <div className="pedido-footer">
                    {/*<div className="icon-pointer">*/}
                    {/*    <img src={mesaIcon} alt="Mesa" style={{ width: 20, height: 20 }} />*/}
                    {/*</div>*/}
                    {/*<div className="icon-pointer" onClick={onClose}>*/}
                    {/*    <img src={cerrarIcon} alt="Cerrar" style={{ width: 14, height: 14 }} />*/}
                    {/*</div>*/}
                    <button className="modal-button" onClick={onClose}>Cerrar</button>
                    {/*<div className="icon-pointer" onClick={() => abrirModalNotas(pedido)}>*/}
                    {/*    <img src={notasIcon} alt="Notas" style={{ width: 20, height: 20 }} />*/}
                    {/*</div>*/}
                </div>
            </div>
        </div>
    );
};

export default PedidoModal;
