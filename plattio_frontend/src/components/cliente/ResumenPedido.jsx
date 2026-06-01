import React, { useState } from 'react';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import '../../styles/ResumenPedido.css';
import logoPlattio from '../../assets/logo_plattio.png';
import Swal from 'sweetalert2';


const ResumenPedido = () => {
    const { numeroMesa } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const pedidoOriginal = location.state?.pedido || {};
    const platos = location.state?.platos || [];

    const categoriasEditables = ['entrada', 'principal'];
    const ordenDeseado = ['entrada', 'principal', 'bebida', 'postre'];
    const esMozo = new URLSearchParams(location.search).get("mozo") === "true";

    const [pedidoEditable, setPedidoEditable] = useState(() => {
        const editable = {};
        for (const [platoId, cantidad] of Object.entries(pedidoOriginal)) {
            const plato = platos.find(p => p.id === parseInt(platoId));
            editable[platoId] = {
                cantidad,
                categoria: plato?.categoria || 'principal'
            };
        }
        return editable;
    });

    const categoriasPresentes = Array.from(
        new Set(Object.values(pedidoEditable).map(p => p.categoria))
    );

    const groupedByCategoria = categoriasPresentes.reduce((acc, cat) => {
        acc[cat] = platos.filter(p => {
            const item = pedidoEditable[p.id];
            return item && item.categoria === cat;
        });
        return acc;
    }, {});

    const total = Object.entries(pedidoEditable).reduce((acc, [platoId, data]) => {
        const plato = platos.find(p => p.id === parseInt(platoId));
        return acc + (plato ? plato.precio * data.cantidad : 0);
    }, 0);

    const handleCategoriaChange = (platoId, nuevaCategoria) => {
        setPedidoEditable(prev => ({
            ...prev,
            [platoId]: {
                ...prev[platoId],
                categoria: nuevaCategoria
            }
        }));
    };

    const pagarEnEfectivo = async () => {
        const sesionId = new URLSearchParams(location.search).get("s");
        if (!sesionId) {
            Swal.fire("Error", "No se encontró la sesión activa.", "error");
            return;
        }

        try {
            const mensaje = `La mesa ${numeroMesa} realizó un pedido en efectivo`;
            const tipo = "Confirmar orden";
            const empleado = JSON.parse(localStorage.getItem("empleado"));

            if (!empleado?.id) {
                Swal.fire("Error", "Empleado no encontrado.", "error");
                return;
            }

            const url = `http://localhost:8080/notificaciones/${encodeURIComponent(mensaje)}/${encodeURIComponent(tipo)}/${empleado.id}/${sesionId}`;
            const res = await fetch(url, { method: "POST" });

            if (!res.ok) throw new Error("Error al enviar notificación");

            await Swal.fire({
                icon: "success",
                title: "El mozo está en camino para confirmar su pedido.",
                //text: "P",
                confirmButtonText: "Aceptar",
                customClass: {
                    confirmButton: "swal-btn-custom"
                },
                buttonsStyling: false
            });

            navigate(`/mesa/${numeroMesa}`);
        } catch (error) {
            console.error("Error:", error);
            Swal.fire("Error", "No se pudo enviar la notificación al mozo.", "error");
        }
    };


    const handleEnviar = async () => {
        try {
            const pedidosPorCategoria = {};

            for (const [platoId, info] of Object.entries(pedidoEditable)) {
                if (!pedidosPorCategoria[info.categoria]) {
                    pedidosPorCategoria[info.categoria] = [];
                }
                pedidosPorCategoria[info.categoria].push({
                    platoId: parseInt(platoId),
                    cantidad: info.cantidad,
                    nota: ''
                });
            }

            const sesionId = new URLSearchParams(location.search).get("s");
            if (!sesionId) throw new Error("Sesión no encontrada");

            const esMozo = new URLSearchParams(location.search).get("mozo") === "true";

            for (const [categoria, items] of Object.entries(pedidosPorCategoria)) {
                if (items.length === 0) continue;

                const crearRes = await fetch(`http://localhost:8080/pedidos/${sesionId}/${categoria}`, {
                    method: 'POST'
                });

                if (!crearRes.ok) throw new Error(`Error creando pedido para ${categoria}`);

                const pedidosRes = await fetch(`http://localhost:8080/pedidos/sesion/${sesionId}`);
                if (!pedidosRes.ok) throw new Error("No se pudo obtener los pedidos");

                const pedidos = await pedidosRes.json();
                const nuevoPedido = pedidos[pedidos.length - 1];

                if (!nuevoPedido?.id) throw new Error("No se encontró el ID del nuevo pedido");

                for (const item of items) {
                    const safeNota = item.nota?.trim() || "sin-nota";
                    const resItem = await fetch(
                        `http://localhost:8080/items/${nuevoPedido.id}/${item.platoId}/${item.cantidad}/${encodeURIComponent(safeNota)}`,
                        { method: 'POST' }
                    );

                    if (!resItem.ok) throw new Error("Error agregando ítem al pedido");
                }
            }

            await Swal.fire({
                icon: "success",
                title: "¡Pedido realizado correctamente!",
                confirmButtonText: "Aceptar",
                customClass: {
                    confirmButton: "swal-btn-custom"
                },
                buttonsStyling: false
            });

            navigate(esMozo ? `/mesa/${numeroMesa}?mozo=true` : `/mesa/${numeroMesa}`);

        } catch (error) {
            console.error(error);
            alert("Hubo un error al crear los pedidos");
        }
    };


    return (
        <div className="resumen-container">
            <div className="resumen-header">
                <img src={logoPlattio} alt="Plattio" className="logo" />
                <h2>Resumen del pedido</h2>
            </div>

            {categoriasPresentes
                .sort((a, b) => ordenDeseado.indexOf(a) - ordenDeseado.indexOf(b))
                .map(cat => (
                    groupedByCategoria[cat]?.length > 0 && (
                        <div key={cat} className="categoria-resumen">
                            <h3 className="categoria-titulo">{cat.charAt(0).toUpperCase() + cat.slice(1)}</h3>
                            {groupedByCategoria[cat].map(plato => (
                                <div className="plato-resumen" key={plato.id}>
                                    <div className="info">
                                        <strong>{plato.nombre}</strong>
                                        <div className="detalle">{plato.descripcion}</div>
                                    </div>
                                    <div className="cantidad">x{pedidoEditable[plato.id].cantidad}</div>
                                    <div className="precio">${plato.precio.toLocaleString('es-AR')}</div>

                                    {categoriasEditables.includes(pedidoEditable[plato.id].categoria) && (
                                        <select
                                            className="selector-categoria"
                                            value={pedidoEditable[plato.id].categoria}
                                            onChange={(e) => handleCategoriaChange(plato.id, e.target.value)}
                                        >
                                            {categoriasEditables.map(cat => (
                                                <option key={cat} value={cat}>
                                                    {cat.charAt(0).toUpperCase() + cat.slice(1)}
                                                </option>
                                            ))}
                                        </select>
                                    )}
                                </div>
                            ))}
                        </div>
                    )
                ))}

            <div className="resumen-footer">
                <div className="resumen-total">${total.toLocaleString('es-AR')}</div>
                <div className="resumen-botones">
                    {esMozo ? (
                        <button className="pagar" onClick={handleEnviar}>Cargar pedido</button>
                    ) : (
                        <div className="bloque-pago">
                            <div className="titulo-pago">Pagar con:</div>
                            <div className="botones-horizontal">
                                <button className="pagar" onClick={handleEnviar}>Tarjeta/Transferencia</button>
                                <button className="mozo" onClick={pagarEnEfectivo}>Efectivo</button>
                            </div>
                        </div>

                    )}
                </div>
            </div>



        </div>
    );
};

export default ResumenPedido;

