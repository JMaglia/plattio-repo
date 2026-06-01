import React, { useEffect, useState } from 'react';
import MesaCard from './MesaCard';
import MozoNavBar from './MozoNavBar';
import '../../styles/MozoBoard.css';
import logoPlattio from '../../assets/logo_plattio.png';


const MozoBoard = () => {
    const [mesas, setMesas] = useState([]);
    const [filtro, setFiltro] = useState("mis-mesas");
    const empleado = JSON.parse(localStorage.getItem("empleado"));
    const mozoId = empleado?.id;

    useEffect(() => {
        cargarMesas();
    }, [filtro]);

    const cargarMesas = () => {
        const endpoint = filtro === "todas"
            ? "http://localhost:8080/sesiones/activas"
            : `http://localhost:8080/sesiones/mozo/${mozoId}/activas`;

        fetch(endpoint)
            .then(res => res.json())
            .then(data => {
                const formateadas = data.map(m => ({
                    numero: m.numeroMesa,
                    pedidos: agruparPedidos(m.pedidos),
                    totalPedidos: m.pedidos.length,
                    totalProductos: m.pedidos.reduce((acc, p) =>
                        acc + p.items.reduce((s, i) => s + i.cantidad, 0), 0)
                }));
                setMesas(formateadas);
            })
            .catch(err => console.error(err));
    };


    const agruparPedidos = (pedidos) => {
        const resumen = [];

        pedidos.forEach(p => {
            const platos = p.items.map(i => `${i.nombre}${i.cantidad > 1 ? ` x${i.cantidad}` : ''}`);
            const linea = {
                categoria: capitalizar(p.categoria),
                descripcion: platos.join(', ')
            };
            resumen.push(linea);
        });

        return resumen;
    };

    const capitalizar = (texto) => texto.charAt(0).toUpperCase() + texto.slice(1);

    return (
        <div className="mozo-board">
            <div className="encabezado-mozo">
                <img src={logoPlattio} alt="Plattio" className="logo" />
                <h2 className="separador">Mesas</h2>
            </div>

            <div className="botones-filtro">
                <button
                    className={filtro === "mis-mesas" ? "activo" : ""}
                    onClick={() => setFiltro("mis-mesas")}
                >
                    Mis mesas
                </button>
                <button
                    className={filtro === "todas" ? "activo" : ""}
                    onClick={() => setFiltro("todas")}
                >
                    Todas
                </button>
            </div>

            <div className="lista-mesas">
                {mesas.map((mesa, i) => (
                    <MesaCard
                        key={i}
                        numero={mesa.numero}
                        pedidos={mesa.pedidos}
                        totalPedidos={mesa.totalPedidos}
                        totalProductos={mesa.totalProductos}
                    />
                ))}
            </div>

            <MozoNavBar activo="mesas" />
        </div>
    );
};

export default MozoBoard;