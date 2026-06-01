import React, { useState, useEffect } from 'react';
import PedidoColumn from './PedidoColumn';
import OrdenamientoBar from './OrdenamientoBar';
import ModalPedido from './ModalPedido';
import '../styles/CocinaBoard.css';

const CocinaBoard = () => {
    const [pendientes, setPendientes] = useState([]);
    const [enProceso, setEnProceso] = useState([]);

    const [ordenPendientes, setOrdenPendientes] = useState("llegada");
    const [ordenProceso, setOrdenProceso] = useState("llegada");

    const [modalNotasAbierto, setModalNotasAbierto] = useState(false);
    const [platosConNotas, setPlatosConNotas] = useState([]);

    const [pedidoSeleccionadoModal, setPedidoSeleccionadoModal] = useState(null);

    useEffect(() => {
        loadPedidos("pendiente", ordenPendientes);
        loadPedidos("en_preparacion", ordenProceso);
    }, []);

    const onMarcarPlato = (pedidoId, itemId) => {
        console.log(`Marcar como listo el item ${itemId} del pedido ${pedidoId}`);
    };

    const onAbrirModal = (pedido) => {
        setPedidoSeleccionadoModal(pedido);
    };

    const cerrarModalPedido = () => {
        setPedidoSeleccionadoModal(null);
    };


    const abrirModalNotas = (pedido) => {
        const platos = pedido.items.filter(item => item.nota && item.nota.trim() !== "");
        setPlatosConNotas(platos);
        setModalNotasAbierto(true);
    };

    const cerrarModalNotas = () => {
        setModalNotasAbierto(false);
        setPlatosConNotas([]);
    };

    /*const onCambiarEstado = (pedidoId, nuevoEstado) => {
        fetch(`http://localhost:8080/pedidos/${pedidoId}/cambiarEstado/${nuevoEstado}`, {
            method: "POST"
        })
            .then(response => {
                if (!response.ok) throw new Error("Error al cambiar el estado");
                return response.text();
            })
            .then(data => {
                console.log("Respuesta del servidor:", data);
                loadPedidos("pendiente", ordenPendientes);
                loadPedidos("en_preparacion", ordenProceso);
            })
            .catch(error => {
                console.error("Error:", error);
            });
    };
    */

    const onCambiarEstado = (pedidoId, nuevoEstado) => {
        fetch(`http://localhost:8080/pedidos/${pedidoId}/cambiarEstado/${nuevoEstado}`, {
            method: "POST"
        })
            .then(response => {
                if (!response.ok) throw new Error("Error al cambiar el estado");
                return response.json(); // ✅ Usamos el pedido actualizado
            })
            .then(pedidoActualizado => {
                const maxTiempo = Math.max(...pedidoActualizado.items.map(i => i.tiempo));
                const tiempoAjustado = Math.ceil(maxTiempo * 1.3);
                const tiempoFormateado = `${String(tiempoAjustado).padStart(2, '0')}:00`;

                const pedidoFormateado = {
                    id: pedidoActualizado.id,
                    mesa: pedidoActualizado.numMesa,
                    estado: pedidoActualizado.estado === "pendiente" ? "Comenzar" : "Detener",
                    estadoReal: pedidoActualizado.estado,
                    fechaInicio: parseFechaStr(pedidoActualizado.fechaInicio),
                    fechaPreparacion: pedidoActualizado.fechaPreparacion
                        ? parseFechaStr(pedidoActualizado.fechaPreparacion)
                        : null,
                    categoria: pedidoActualizado.categoria,
                    tiempo: tiempoFormateado,
                    items: pedidoActualizado.items.map(i => ({
                        id: i.id,
                        nombre: i.nombre,
                        detalle: i.detalle,
                        tiempo: i.tiempo,
                        nota: i.nota,
                        finalizado: i.finalizado,
                        cantidad: i.cantidad
                    }))
                };

                if (pedidoFormateado.estadoReal === "en_preparacion") {
                    setEnProceso(prev => [...prev, pedidoFormateado]);
                    setPendientes(prev => prev.filter(p => p.id !== pedidoFormateado.id));
                } else if (pedidoFormateado.estadoReal === "pendiente") {
                    setPendientes(prev => [...prev, pedidoFormateado]);
                    setEnProceso(prev => prev.filter(p => p.id !== pedidoFormateado.id));
                } else if (pedidoFormateado.estadoReal === "listo") {
                    setEnProceso(prev => prev.filter(p => p.id !== pedidoFormateado.id));

                    // 🔔 Enviar notificación al mozo
                    const mensaje = `${pedidoFormateado.categoria} está listo`;
                    const tipo = "Pedido listo";
                    const url = `http://localhost:8080/notificaciones/pedido/${encodeURIComponent(mensaje)}/${tipo}/${pedidoFormateado.mesa}/${pedidoFormateado.id}`;

                    fetch(url, { method: "POST" })
                        .then(res => {
                            if (!res.ok) throw new Error("Error al enviar notificación");
                            console.log("✅ Notificación enviada al mozo");
                        })
                        .catch(err => console.error("Error notificando al mozo:", err));
                }
                // Ordenamos de nuevo si hacía falta
                ordenarPor(`${ordenPendientes}-pendientes`);
                ordenarPor(`${ordenProceso}-proceso`);
            })
            .catch(error => {
                console.error("Error:", error);
            });
    };




    const loadPedidos = (estado) => {
        fetch(`http://localhost:8080/pedidos/estado/${estado}`)
            .then(response => {
                if (!response.ok) throw new Error("Error al obtener los pedidos");
                return response.json();
            })
            .then(data => {
                const pedidosFormateados = data.map(p => {
                    const maxTiempo = Math.max(...p.items.map(i => i.tiempo));
                    const tiempoAjustado = Math.ceil(maxTiempo * 1.3);
                    const tiempoFormateado = `${String(tiempoAjustado).padStart(2, '0')}:00`;

                    let tituloBoton = p.estado === "pendiente" ? "Comenzar" : "Detener";

                    return {
                        id: p.id,
                        mesa: p.numMesa,
                        estado: tituloBoton,
                        estadoReal: p.estado,
                        fechaInicio: parseFechaStr(p.fechaInicio),
                        fechaPreparacion: p.fechaPreparacion ? parseFechaStr(p.fechaPreparacion) : null,
                        categoria: p.categoria,
                        tiempo: tiempoFormateado,
                        items: p.items.map(i => ({
                            id: i.id,
                            nombre: i.nombre,
                            detalle: i.detalle,
                            tiempo: i.tiempo,
                            nota: i.nota,
                            finalizado: i.finalizado,
                            cantidad: i.cantidad
                        }))
                    };
                });

                const pedidosSinSoloBebidas = pedidosFormateados.filter(p =>
                    (p.categoria || "").toLowerCase().trim() !== "bebida"
                );

                if (estado === "pendiente") {
                    setPendientes(pedidosSinSoloBebidas);
                    ordenarPor(`${ordenPendientes}-pendientes`);
                } else if (estado === "en_preparacion") {
                    setEnProceso(pedidosSinSoloBebidas);
                    ordenarPor(`${ordenProceso}-proceso`);
                }
            })
            .catch(error => console.error("Error cargando pedidos:", error));
    };

    const ordenarPor = (criterio) => {
        if (criterio === 'llegada-pendientes') {
            setOrdenPendientes("llegada");
            setPendientes(prev => [...prev].sort((a, b) => a.fechaInicio - b.fechaInicio));
        }

        if (criterio === 'llegada-proceso') {
            setOrdenProceso("llegada");
            setEnProceso(prev => [...prev].sort((a, b) => a.fechaInicio - b.fechaInicio));
        }

        if (criterio === 'timer-pendientes') {
            setOrdenPendientes("timer");
            setPendientes(prev => [...prev].sort((a, b) => tiempoASegundos(a.tiempo) - tiempoASegundos(b.tiempo)));
        }

        if (criterio === 'timer-proceso') {
            setOrdenProceso("timer");
            setEnProceso(prev => [...prev].sort((a, b) => tiempoASegundos(a.tiempo) - tiempoASegundos(b.tiempo)));
        }

        if (criterio === 'inteligente-pendientes') {
            setOrdenPendientes("inteligente");
            setPendientes(prev => {
                const ordenados = ordenarInteligente(prev, enProceso);
                console.log("📦 Pedidos ordenados inteligentemente:", ordenados);
                return ordenados;
            });
        }

    };

    const parseFechaStr = (fechaStr) => {
        if (typeof fechaStr !== "string") return new Date("Invalid");
        const [fecha, hora] = fechaStr.split(" ");
        if (!fecha || !hora) return new Date("Invalid");
        const [dd, mm, yyyy] = fecha.split("/");
        return new Date(`${yyyy}-${mm}-${dd}T${hora}`);
    };

    const tiempoASegundos = (tiempoStr) => {
        if (typeof tiempoStr !== "string") return 0;
        const [min, seg] = tiempoStr.split(":").map(Number);
        return (min * 60) + seg;
    };

    /*
    const ordenarInteligente = (pendientes, enProceso) => {
        const ahora = new Date();

        const enProcesoPorMesa = {};
        enProceso.forEach(p => {
            if (!enProcesoPorMesa[p.mesa]) enProcesoPorMesa[p.mesa] = [];
            enProcesoPorMesa[p.mesa].push(p);
        });

        const calcularScore = (pedido) => {
            const minutosDesdeInicio = Math.floor((ahora - pedido.fechaInicio) / 60000);
            let score = 0;
            score += 1.5 * minutosDesdeInicio;

            const tiempoMaximo = Math.max(...pedido.items.map(i => i.tiempo || 0));
            score += 1.2 * tiempoMaximo * 1.3;

            if (pedido.items.some(i => i.nota && i.nota.trim() !== '')) score += 5;

            const mesaEnProceso = enProcesoPorMesa[pedido.mesa] || [];

            if (pedido.categoria === 'entrada' && mesaEnProceso.some(p => p.categoria === 'principal')) score += 50;
            if (pedido.categoria === 'principal' && mesaEnProceso.some(p => p.categoria === 'principal')) score -= 10;
            if (mesaEnProceso.length > 0) score += 10;

            const totalItems = pedido.items.reduce((sum, i) => sum + (i.cantidad || 0), 0);
            score += 0.5 * totalItems;

            return score;
        };

        return [...pendientes]
            .map(p => ({ ...p, score: calcularScore(p) }))
            .sort((a, b) => b.score - a.score);
    };
    */

    const ordenarInteligente = (pendientes, enProceso) => {
        const ahora = new Date();

        const enProcesoPorMesa = {};
        enProceso.forEach(p => {
            if (!enProcesoPorMesa[p.mesa]) enProcesoPorMesa[p.mesa] = [];
            enProcesoPorMesa[p.mesa].push(p);
        });

        const calcularScore = (todosPendientes, pedido) => {
            const minutosDesdeInicio = Math.floor((ahora - pedido.fechaInicio) / 60000);
            let score = 0;
            score += 1.5 * minutosDesdeInicio;

            const tiempoMaximo = Math.max(...pedido.items.map(i => i.tiempo || 0));
            score += 1.2 * tiempoMaximo * 1.3;

            if (pedido.items.some(i => i.nota && i.nota.trim() !== '')) score += 5;

            const mesaEnProceso = enProcesoPorMesa[pedido.mesa] || [];

            // 👑 Prioridad máxima si hay principal en proceso
            if (pedido.categoria === 'entrada' && mesaEnProceso.some(p => p.categoria === 'principal')) {
                score += 50;
            }

            // 💡 Prioridad media si hay principal pendiente
            if (
                pedido.categoria === 'entrada' &&
                !mesaEnProceso.some(p => p.categoria === 'principal') &&
                todosPendientes.some(p =>
                    p.id !== pedido.id &&
                    p.mesa === pedido.mesa &&
                    p.categoria === 'principal'
                )
            ) {
                score += 20;
            }

            if (pedido.categoria === 'principal' && mesaEnProceso.some(p => p.categoria === 'principal')) score -= 10;
            if (mesaEnProceso.length > 0) score += 10;

            const totalItems = pedido.items.reduce((sum, i) => sum + (i.cantidad || 0), 0);
            score += 0.5 * totalItems;

            return score;
        };

        return [...pendientes]
            .map(p => ({ ...p, score: calcularScore(pendientes, p) }))
            .sort((a, b) => b.score - a.score);
    };



    return (
        <div className="cocina-container">
            <OrdenamientoBar ordenarPor={ordenarPor} />
            <div className="cocina-tablero">
                <PedidoColumn
                    titulo="Pendientes"
                    pedidos={pendientes}
                    onMarcarPlato={onMarcarPlato}
                    onAbrirModal={onAbrirModal}
                    abrirModalNotas={abrirModalNotas}
                    onCambiarEstado={onCambiarEstado}
                />
                <PedidoColumn
                    titulo="En proceso"
                    pedidos={enProceso}
                    onMarcarPlato={onMarcarPlato}
                    onAbrirModal={onAbrirModal}
                    abrirModalNotas={abrirModalNotas}
                    onCambiarEstado={onCambiarEstado}
                />
            </div>

            {modalNotasAbierto && (
                <div className="modal-backdrop">
                    <div className="modal">
                        <h3>Notas del pedido</h3>
                        {platosConNotas.length > 0 ? (
                            <ul>
                                {platosConNotas.map((plato, index) => (
                                    <li key={index}>
                                        <strong>{plato.nombre}</strong>: {plato.nota}
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p>No hay notas.</p>
                        )}
                        <button onClick={cerrarModalNotas}>Cerrar</button>
                    </div>
                </div>
            )}

            {pedidoSeleccionadoModal && (
                <ModalPedido
                    pedido={pedidoSeleccionadoModal}
                    onClose={cerrarModalPedido}
                    onMarcarPlato={onMarcarPlato}
                    onCambiarEstado={onCambiarEstado}
                    abrirModalNotas={abrirModalNotas}
                />
            )}
        </div>
    );
};

export default CocinaBoard;