import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import MozoNavBar from './MozoNavBar';
import logoPlattio from '../../assets/logo_plattio.png';
import '../../styles/NotificacionesMozo.css';

const NotificacionesMozo = () => {
    const [filtro, setFiltro] = useState('pendiente'); // ← en singular, como espera el backend
    const [notificaciones, setNotificaciones] = useState([]);
    const empleado = JSON.parse(localStorage.getItem("empleado"));
    const idMozo = empleado?.id;
    const navigate = useNavigate();

    useEffect(() => {
        if (!idMozo) return;
        fetch(`http://localhost:8080/notificaciones/mozo/${idMozo}/estado/${filtro}`)
            .then(res => res.json())
            .then(data => {
                if (Array.isArray(data)) {
                    setNotificaciones(data);
                } else {
                    setNotificaciones([]);
                    console.warn("La respuesta no es un array:", data);
                }
            })
            .catch(err => {
                console.error("Error al cargar notificaciones:", err);
                setNotificaciones([]);
            });
    }, [idMozo, filtro]);

    const completarNotificacion = (id) => {
        fetch(`http://localhost:8080/notificaciones/${id}/toggleEstado`, {
            method: 'POST'
        })
            .then(res => {
                if (res.ok) {
                    setNotificaciones(prev =>
                        prev.map(n => n.id === id
                            ? { ...n, estado: n.estado === "completada" ? "pendiente" : "completada" }
                            : n
                        )
                    );
                }
            });
    };

    const irAPedidosListos = () => {
        navigate(`/mozo/pedidos/${idMozo}?filtro=listos`);
    };

    return (
        <div className="notificaciones-mozo">
            <div className="encabezado-mozo">
                <img src={logoPlattio} alt="Plattio" className="logo" />
                <h2 className="separador">Notificaciones</h2>
            </div>

            <div className="botones-filtro">
                <button className={filtro === "pendiente" ? "activo" : ""} onClick={() => setFiltro("pendiente")}>
                    Pendientes
                </button>
                <button className={filtro === "completada" ? "activo" : ""} onClick={() => setFiltro("completada")}>
                    Completadas
                </button>
            </div>

            <div className="lista-notificaciones">
                {notificaciones.map(n => (
                    <div key={n.id} className="notificacion-item">
                        <span>Mesa {n.numeroMesa} - {n.tipo}</span>
                        {n.tipo === "Pedido listo" ? (
                            <span className="flecha" onClick={irAPedidosListos} style={{ cursor: "pointer" }}>→</span>
                        ) : (
                            <input
                                type="checkbox"
                                checked={n.estado === "completada"}
                                onChange={() => completarNotificacion(n.id)}
                            />
                        )}
                    </div>
                ))}
            </div>

            <MozoNavBar activo="notificaciones" />
        </div>
    );
};

export default NotificacionesMozo;
