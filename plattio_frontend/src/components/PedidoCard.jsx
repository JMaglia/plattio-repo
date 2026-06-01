import React, { useEffect, useState } from 'react';
import '../styles/PedidoCard.css';
import expandirIcon from '../assets/icons/expandir.png';
import notasIcon from '../assets/icons/notas.png';
import mesaIcon from '../assets/icons/mesa.png';
import deliveryIcon from '../assets/icons/delivery.png';

const PedidoCard = ({ pedido, onMarcarPlato, onAbrirModal, abrirModalNotas, onCambiarEstado, modo }) => {
    const [tiempoRestante, setTiempoRestante] = useState(pedido.tiempo);

    useEffect(() => {
        if (pedido.estadoReal !== 'en_preparacion' || !pedido.fechaPreparacion) {
            setTiempoRestante(pedido.tiempo);
            return;
        }

        const tiempoEstimadoSegundos = tiempoStringASegundos(pedido.tiempo);

        const actualizar = () => {
            const ahora = new Date();
            const inicio = new Date(pedido.fechaPreparacion);
            const diferencia = Math.floor((ahora - inicio) / 1000);
            const restante = Math.max(tiempoEstimadoSegundos - diferencia, 0);
            setTiempoRestante(segundosAString(restante));
        };

        actualizar(); // ⏱️ llama una vez al inicio
        const intervalo = setInterval(actualizar, 1000);

        return () => clearInterval(intervalo); // cleanup
    }, [pedido.fechaPreparacion, pedido.estadoReal, pedido.tiempo]);


    const tiempoStringASegundos = (str) => {
        const [min, seg] = str.split(":").map(Number);
        return (min * 60) + (seg || 0);
    };

    const segundosAString = (totalSeg) => {
        const min = Math.floor(totalSeg / 60);
        const seg = totalSeg % 60;
        return `${String(min).padStart(2, "0")}:${String(seg).padStart(2, "0")}`;
    };

    return (
        <div className="pedido-card">
            <div className="pedido-header">
                <div className="header-left">
                    <div className="icon-pointer" onClick={() => onAbrirModal(pedido)}>
                        <img src={expandirIcon} alt="Expandir" style={{ width: 14, height: 14 }} />
                    </div>
                    <div className="mesa-numero">
                        {pedido.mesa === 0 ? (
                            <img src={deliveryIcon} alt="Sin mesa" style={{ width: 18, height: 18 }} />
                        ) : (
                            pedido.mesa
                        )}
                    </div>
                </div>

                <div className="header-center">
                    {modo === "mozo" ? (
                        <span className="estado-label">{pedido.estadoReal}</span>
                    ) : (
                        <button
                            className="estado-boton"
                            onClick={() => onCambiarEstado(
                                pedido.id,
                                pedido.estado === "Comenzar" ? "en_preparacion" : "pendiente"
                            )}
                        >
                            {pedido.estado}
                        </button>
                    )}
                </div>

                <div className="header-right">
                    <div className="hora-pedido">{tiempoRestante}</div>
                </div>
            </div>

            <div className="pedido-items-scroll">
                {pedido.items.map((item, i) => (
                    <div className="plato-item" key={i}>
                        <div className="plato-info">
                            <div className="plato-nombre">{item.nombre}</div>
                            <div className="plato-detalle">{item.detalle}</div>
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
                <div className="icon-pointer" title="Input aún no disponible">
                    <img src={mesaIcon} alt="Mesa" style={{ width: 20, height: 20 }} />
                </div>
                {modo !== 'mozo' && pedido.estadoReal === 'en_preparacion' && (
                    <button className="btn-entregar" onClick={() => onCambiarEstado(pedido.id, "listo")}>
                        Listo
                    </button>
                )}
                {modo === 'mozo' && (pedido.estadoReal === 'listo' || pedido.categoria?.toLowerCase() === 'bebida') && (
                    <button className="btn-entregar" onClick={() => onCambiarEstado(pedido.id, "finalizado")}>
                        Finalizar
                    </button>
                )}
                <div className="icon-pointer" onClick={() => abrirModalNotas(pedido)}>
                    <img src={notasIcon} alt="Notas" style={{ width: 20, height: 20 }} />
                </div>
            </div>
        </div>
    );
};

export default PedidoCard;
