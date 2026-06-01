import React, { useEffect, useRef, useState } from 'react';
import '../../styles/MozoEditarCarta.css';
import EditarPlatoModal from './EditarPlatoModal';
import MozoNavBar from './MozoNavBar';
import iconEditar from '../../assets/icons/edittt.png';
import iconEliminar from '../../assets/icons/eliminar.png';
import editarIcon from '../../assets/icons/edittt.png';
import tableroIcon from '../../assets/icons/tablero.png';
import { useNavigate, useLocation } from 'react-router-dom';

const EditarCarta = () => {
    const [platos, setPlatos] = useState([]);
    const seccionesRef = useRef({});
    const categorias = ['entrada', 'principal', 'bebida', 'postre'];
    const [platoEditarId, setPlatoEditarId] = useState(null);
    const [modoNuevo, setModoNuevo] = useState(null);
    const location = useLocation();
    const desdeCocina = new URLSearchParams(location.search).get("origen") === "cocina";
    const navigate = useNavigate();

    const [menuVisible, setMenuVisible] = useState(false);

    const fetchPlatos = () => {
        fetch('http://localhost:8080/platos')
            .then(res => res.json())
            .then(data => setPlatos(data))
            .catch(err => console.error(err));
    };

    useEffect(() => {
        fetchPlatos();
    }, []);

    const toggleMenu = () => {
        setMenuVisible(prev => !prev);
    };

    const handleIrACocina = () => {
        setMenuVisible(false);
        navigate('/cocina');
    };

    const handleIrAEditarCarta = () => {
        setMenuVisible(false);
        navigate('/mozo/editar-carta');
    };

    const scrollToCategoria = (categoria) => {
        const section = seccionesRef.current[categoria];
        if (section) {
            const yOffset = -160;
            const y = section.getBoundingClientRect().top + window.pageYOffset + yOffset;
            window.scrollTo({ top: y, behavior: 'smooth' });
        }
    };

    const handleEliminar = async (id) => {
        const confirmacion = window.confirm("¿Eliminar este plato?");
        if (!confirmacion) return;

        try {
            await fetch(`http://localhost:8080/platos/${id}`, { method: 'DELETE' });
            setPlatos(prev => prev.filter(p => p.id !== id));
        } catch (err) {
            console.error("Error al eliminar plato:", err);
        }
    };

    const toggleActivo = async (id) => {
        try {
            await fetch(`http://localhost:8080/platos/${id}/toggleActivo`, {
                method: 'POST'
            });
            fetchPlatos(); // refresca lista
        } catch (err) {
            console.error("Error al cambiar estado:", err);
        }
    };

    return (
        <div className="editar-carta-container">
            {desdeCocina && (
                <div className="menu-container">
                    <button className="menu-btn-2" onClick={toggleMenu}>☰</button>
                    {menuVisible && (
                        <div className="menu-dropdown">
                            <div onClick={() => navigate('/cocina')}>
                                <img src={tableroIcon} alt="Tablero" className="menu-icon" />
                                Ver tablero de comandas
                            </div>
                            <hr className="menu-divider" />
                            <div onClick={() => navigate('/editarcarta?origen=cocina')}>
                                <img src={editarIcon} alt="Editar" className="menu-icon" />
                                Editar carta
                            </div>
                        </div>
                    )}
                </div>
            )}
            <header className="encabezado-mozo">
                <img src={require('../../assets/logo_plattio.png')} alt="Logo Plattio" className="logo" />
                <h2 className="separador">Editar carta</h2>


            </header>

            <nav className="editar-carta-nav">
                {categorias.map(cat => (
                    <button key={cat} onClick={() => scrollToCategoria(cat)}>
                        {cat === 'principal' ? 'Principal' : cat.charAt(0).toUpperCase() + cat.slice(1)}
                    </button>
                ))}
            </nav>

            <main className="editar-carta-secciones">
                {categorias.map(cat => {
                    const platosCat = platos.filter(p => p.categoria === cat);
                    return (
                        <section key={cat} ref={el => seccionesRef.current[cat] = el}>
                            <h3 className="categoria-titulo">{cat === 'principal' ? 'Plato principal' : cat.charAt(0).toUpperCase() + cat.slice(1)}</h3>
                            {platosCat.map(plato => (
                                <div key={plato.id} className="fila-plato">
                                    <div className="plato-info">
                                        <h4>{plato.nombre}</h4>
                                        <p>{plato.descripcion}</p>
                                    </div>
                                    <div className="acciones">
                                        <img
                                            src={require(`../../assets/icons/${plato.activoEnCarta ? 'activo' : 'desactivado'}.png`)}
                                            alt={plato.activoEnCarta ? 'Activo' : 'Desactivado'}
                                            title={plato.activoEnCarta ? 'Plato activo en carta' : 'Plato oculto en carta'}
                                            onClick={() => toggleActivo(plato.id)}
                                        />
                                        <img
                                            src={iconEditar}
                                            alt="Editar"
                                            title="Editar"
                                            onClick={() => setPlatoEditarId(plato.id)}
                                        />
                                        <img
                                            src={iconEliminar}
                                            alt="Eliminar"
                                            title="Eliminar"
                                            onClick={() => handleEliminar(plato.id)}
                                        />
                                        <span className="precio">${plato.precio.toLocaleString('es-AR')}</span>
                                    </div>
                                </div>
                            ))}
                            <img
                                src={require(`../../assets/icons/sumar.png`)}
                                alt="Agregar"
                                title="Agregar"
                                onClick={() => setModoNuevo(cat)}
                                className="agregar-btn"
                            />
                        </section>
                    );
                })}
            </main>

            {platoEditarId && (
                <EditarPlatoModal
                    id={platoEditarId}
                    onClose={() => setPlatoEditarId(null)}
                    onGuardado={fetchPlatos}
                />
            )}
            {modoNuevo && (
                <EditarPlatoModal
                    esNuevo={true}
                    onClose={() => setModoNuevo(null)}
                    onGuardado={fetchPlatos}
                    categoriaDefault={modoNuevo}
                />
            )}

            {!desdeCocina && <MozoNavBar activo="editarCarta" />}
        </div>
    );
};

export default EditarCarta;
