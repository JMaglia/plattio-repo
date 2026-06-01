import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import PedidoCardCliente from './PedidoCardCliente';
import DerivarMesaModal from '../mozo/DerivarMesaModal';
import '../../styles/MesaView.css';
import logoPlattio from '../../assets/logo_plattio.png';
import mozo from '../../assets/icons/mozo.png';
import llamarMozo from '../../assets/icons/llamarMozo.png';
import pedirCuenta from '../../assets/icons/pedirCuenta.png';
import MozoNavBar from '../mozo/MozoNavBar';
import Swal from 'sweetalert2';


const MesaView = () => {
    const { numeroMesa } = useParams();
    const [sesion, setSesion] = useState(null);
    const [pedidos, setPedidos] = useState([]);
    const [menuVisible, setMenuVisible] = useState(false);
    const [mostrarModal, setMostrarModal] = useState(false);
    const navigate = useNavigate();

    const params = new URLSearchParams(window.location.search);
    const esMozo = params.get("mozo") === "true";

    useEffect(() => {
        window.scrollTo(0, 0);
    }, []);

    useEffect(() => {
        fetch(`http://localhost:8080/sesiones/numeroMesa/${numeroMesa}/activa`)
            .then(res => res.ok ? res.json() : null)
            .then(data => {
                if (data) {
                    setSesion(data);
                    cargarPedidos(data.id);
                }
            })
            .catch(() => setSesion(null));
    }, [numeroMesa]);

    const cargarPedidos = (sesionId) => {
        fetch(`http://localhost:8080/pedidos/sesion/${sesionId}`)
            .then(res => res.ok ? res.json() : [])
            .then(setPedidos)
            .catch(() => setPedidos([]));
    };

    const crearNuevoPedido = () => {
        if (sesion) {
            const query = `?s=${sesion.id}${esMozo ? '&mozo=true' : ''}`;
            navigate(`/mesa/${numeroMesa}/carta${query}`);
        }
    };

    const toggleMenu = () => {
        setMenuVisible(prev => !prev);
    };

    const enviarNotificacion = async (tipo) => {
        const empleado = JSON.parse(localStorage.getItem("empleado"));
        if (!sesion || !empleado?.id) {
            console.warn("Sesión o mozo no disponible");
            return;
        }

        const mensaje = tipo === "Asistir"
            ? "El cliente necesita asistencia"
            : "El cliente pidió la cuenta";

        const url = `http://localhost:8080/notificaciones/${encodeURIComponent(mensaje)}/${encodeURIComponent(tipo)}/${empleado.id}/${sesion.id}`;

        try {
            const res = await fetch(url, { method: "POST" });
            if (!res.ok) throw new Error("Error en la solicitud");
            Swal.fire({
                icon: 'success',
                title: tipo === "Asistir" ? "Notificación de llamado enviada" : "Notificación de pedido de cuenta enviada",
                confirmButtonText: "Aceptar",
                customClass: {
                    confirmButton: "swal-btn-custom"
                },
                buttonsStyling: false
            });
            setMenuVisible(false);
        } catch (error) {
            console.error("Error al enviar la notificación:", error);
        }
    };



    return (
        <div className="mesa-container">
            <img src={logoPlattio} alt="Plattio" className="logo" />
            <h2>Mesa N.º <span className="mesa-numero">{numeroMesa}</span></h2>
            <div className="separador">Pedidos</div>

            {pedidos.filter(pedido => pedido.estado !== "cancelado")
                .map((pedido, i) => (
                    <PedidoCardCliente
                        key={pedido.id}
                        pedido={pedido}
                        numero={i + 1}
                        modo={esMozo ? "mozo" : "cliente"}
                    />
                ))}

            <div className="nuevo-pedido-box" onClick={crearNuevoPedido}>
                <span className="mas-simbolo">+</span>
            </div>

            {esMozo && (
                <>
                    <button className="boton-derivar" onClick={() => setMostrarModal(true)}>
                        Derivar mesa
                    </button>
                    <MozoNavBar activo="mesas" />
                </>
            )}

            {!esMozo && (
                <div className="mozo-icono" onClick={toggleMenu}>
                    <img src={mozo} alt="Pedir mozo" />
                </div>
            )}

            {menuVisible && (
                <div className="mozo-menu">
                    <div
                        className="mozo-menu-opcion"
                        onClick={() => {
                            console.log("Click en llamar al mozo");
                            enviarNotificacion("Asistir");
                        }}
                    >
                        <img src={llamarMozo} alt="Llamar al mozo" className="mozo-menu-icono" />
                        Llamar al mozo
                    </div>
                    <div
                        className="mozo-menu-opcion"
                        onClick={() => {
                            console.log("Click en pedir cuenta");
                            enviarNotificacion("Entregar cuenta");
                        }}
                    >
                        <img src={pedirCuenta} alt="Pedir cuenta" className="mozo-menu-icono" />
                        Pedir cuenta
                    </div>
                </div>
            )}


            <DerivarMesaModal
                visible={mostrarModal}
                numeroMesa={numeroMesa}
                sesionId={sesion?.id}
                onClose={() => setMostrarModal(false)}
                onConfirmar={() => {
                    const empleado = JSON.parse(localStorage.getItem("empleado"));
                    navigate(`/mozo/${empleado.id}`);
                }}
            />
        </div>
    );
};

export default MesaView;
