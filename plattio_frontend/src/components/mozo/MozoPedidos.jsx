import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import PedidoCard from '../PedidoCard';
import MozoNavBar from './MozoNavBar';
import logoPlattio from '../../assets/logo_plattio.png';
import '../../styles/MozoPedidos.css';

const MozoPedidos = () => {
    const location = useLocation();
    const query = new URLSearchParams(location.search);
    const filtroInicial = query.get("filtro") || "pendientes"; // ← esto puede ser "pendientes" o "listos"
    const [filtro, setFiltro] = useState(filtroInicial);
    const [pedidos, setPedidos] = useState([]);
    


    useEffect(() => {
        const empleado = JSON.parse(localStorage.getItem("empleado"));
        if (!empleado?.id) return;

        if (filtro === "pendientes") {
            fetchPedidos(`http://localhost:8080/pedidos/activos/${empleado.id}`);
        } else if (filtro === "listos") {
            fetchPedidos(`http://localhost:8080/pedidos/listos/mozo/${empleado.id}`);
        }
    }, [filtro]);

    const fetchPedidos = async (url) => {
        try {
            const res = await fetch(url);
            const data = await res.json();

            if (!Array.isArray(data)) {
                console.error("Respuesta inválida:", data);
                return;
            }

            const formateados = agruparPorPedido(data);
            setPedidos(formateados);
        } catch (error) {
            console.error("Error al obtener pedidos:", error);
        }
    };

    const agruparPorPedido = (pedidos) => {
        return pedidos.map(p => ({
            id: p.id,
            mesa: p.numMesa,
            estado: p.estado === "en_preparacion" ? "En proceso" : "Comenzar",
            estadoReal: p.estado,
            categoria: p.categoria,
            tiempo: p.tiempo,
            items: p.items.map(item => ({
                id: item.id,
                nombre: item.nombre,
                detalle: item.detalle,
                cantidad: item.cantidad,
                finalizado: item.finalizado
            }))
        }));
    };

    const onCambiarEstado = async (pedidoId, nuevoEstado) => {
        try {
            const res = await fetch(`http://localhost:8080/pedidos/${pedidoId}/cambiarEstado/${nuevoEstado}`, {
                method: "POST",
            });

            
            const empleado = JSON.parse(localStorage.getItem("empleado"));
            if (!empleado?.id) return;

            if (filtro === "pendientes") {
                fetchPedidos(`http://localhost:8080/pedidos/activos/${empleado.id}`);
            } else if (filtro === "listos") {
                fetchPedidos(`http://localhost:8080/pedidos/listos/mozo/${empleado.id}`);
            }
        } catch (error) {
            console.error("Error al cambiar estado:", error);
        }
    };


    return (
        <div className="mozo-pedidos">
            <div className="encabezado-mozo">
                <img src={logoPlattio} alt="Plattio" className="logo" />
                <h2 className="separador">Pedidos</h2>
            </div>

            <div className="botones-filtro">
                <button
                    className={filtro === "pendientes" ? "activo" : ""}
                    onClick={() => setFiltro("pendientes")}
                >
                    Pendientes
                </button>
                <button
                    className={filtro === "listos" ? "activo" : ""}
                    onClick={() => setFiltro("listos")}
                >
                    Listos
                </button>
            </div>

            <div className="lista-pedidos">
                {pedidos.map(p => (
                    <PedidoCard
                        key={p.id}
                        pedido={p}
                        modo="mozo"
                        onMarcarPlato={() => { }}
                        onAbrirModal={() => { }}
                        abrirModalNotas={() => { }}
                        onCambiarEstado={onCambiarEstado}
                    />
                ))}
            </div>

            <MozoNavBar activo="pedidos" />
        </div>
    );
};

export default MozoPedidos;
