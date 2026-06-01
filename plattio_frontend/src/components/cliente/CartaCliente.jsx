import React, { useEffect, useRef, useState } from 'react';
import CategoriaSeccion from './CategoriaSeccion';
import '../../styles/CartaCliente.css';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import Swal from 'sweetalert2';


const CartaCliente = () => {
    const [platos, setPlatos] = useState([]);
    const [pedido, setPedido] = useState({});
    const seccionesRef = useRef({});
    const navigate = useNavigate();
    const { numeroMesa } = useParams();
    const location = useLocation();
    const esMozo = new URLSearchParams(location.search).get("mozo") === "true";

    useEffect(() => {
        fetch('http://localhost:8080/platos/activos')
            .then(res => res.json())
            .then(data => setPlatos(data))
            .catch(err => console.error(err));
    }, []);

    const categorias = ['entrada', 'principal', 'bebida', 'postre'];

    const scrollToCategoria = (categoria) => {
        const section = seccionesRef.current[categoria];
        if (section) {
            const yOffset = -160;
            const y = section.getBoundingClientRect().top + window.pageYOffset + yOffset;
            window.scrollTo({ top: y, behavior: 'smooth' });
        }
    };

    const handleAgregar = (plato) => {
        setPedido(prev => ({
            ...prev,
            [plato.id]: (prev[plato.id] || 0) + 1
        }));
    };

    const handleQuitar = (plato) => {
        setPedido(prev => {
            const copia = { ...prev };
            if (copia[plato.id] > 1) {
                copia[plato.id]--;
            } else {
                delete copia[plato.id];
            }
            return copia;
        });
    };

    const calcularTotal = () => {
        return platos.reduce((acc, plato) => {
            const cant = pedido[plato.id] || 0;
            return acc + cant * plato.precio;
        }, 0);
    };

    const handleVerPedido = () => {
        const sesionId = new URLSearchParams(location.search).get("s");

        if (!sesionId) {
            alert("Sesión no encontrada");
            return;
        }

        if (Object.keys(pedido).length === 0) {
            Swal.fire({
                icon: "warning",
                title: "¡Atención!",
                text: "Debés agregar al menos un plato para continuar.",
                confirmButtonText: "Aceptar",
                customClass: {
                    confirmButton: "swal-btn-custom"
                },
                buttonsStyling: false
            });
            return;
        }


        const query = `?s=${sesionId}${esMozo ? '&mozo=true' : ''}`;
        navigate(`/mesa/${numeroMesa}/resumen${query}`, {
            state: {
                pedido,
                platos
            }
        });
    };

    return (
        <div className="carta-container">
            <div className="carta-top">
                <div className="carta-header">
                    <img src={require('../../assets/logo_plattio.png')} alt="Plattio" className="logo" />
                </div>

                <div className="categorias-nav">
                    {categorias.map(cat => (
                        <button key={cat} onClick={() => scrollToCategoria(cat)}>
                            {cat.charAt(0).toUpperCase() + cat.slice(1)}
                        </button>
                    ))}
                </div>
            </div>

            <div className="categorias-secciones">
                {categorias.map(cat => {
                    const platosCat = platos.filter(p => p.categoria === cat);
                    if (platosCat.length === 0) return null;
                    return (
                        <div key={cat} ref={el => seccionesRef.current[cat] = el}>
                            <CategoriaSeccion
                                titulo={cat}
                                platos={platosCat}
                                pedido={pedido}
                                onAgregar={handleAgregar}
                                onQuitar={handleQuitar}
                            />
                        </div>
                    );
                })}
            </div>

            <div className="total-footer">
                <span className='total-monto'>${calcularTotal().toLocaleString('es-AR')}</span>
                <button className="ver-pedido" onClick={handleVerPedido}>Ver pedido</button>
            </div>

        </div>
    );
};

export default CartaCliente;



//{esMozo && <MozoNavBar activo="editarCarta" />} paulo, para agregar el nav bar del mozo pone esto en la linea 120 