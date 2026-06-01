import React, { useEffect, useState } from 'react';
import '../../styles/ModalPedido.css';

const DerivarMesaModal = ({ visible, onClose, onConfirmar, numeroMesa, sesionId }) => {
    const [mozos, setMozos] = useState([]);
    const [seleccionado, setSeleccionado] = useState('');

    useEffect(() => {
        if (!visible) return;

        const mozoActual = JSON.parse(localStorage.getItem("empleado"));

        fetch("http://localhost:8080/empleados/rol/mozo")
            .then(res => res.ok ? res.json() : [])
            .then(data => {
                const filtrados = data.filter(m => m.id !== mozoActual?.id);
                setMozos(filtrados);
            })
            .catch(() => setMozos([]));
    }, [visible]);

    if (!visible) return null;

    const handleConfirmar = () => {
        if (!seleccionado) return;

        fetch(`http://localhost:8080/sesiones/${sesionId}/reasignarMozo/${seleccionado}`, {
            method: 'POST'
        })
            .then(res => {
                if (!res.ok) throw new Error("Error al derivar");
                return res.text();
            })
            .then(msg => {
                alert(msg);
                onConfirmar(); // Para cerrar el modal y recargar
            })
            .catch(() => alert("No se pudo derivar la mesa"));
    };

    return (
        <div className="modal-backdrop">
            <div className="modal-detalle" style={{ height: 'auto', padding: '2rem', maxWidth: '90%' }}>
                <h3 style={{ textAlign: 'center', color: '#d3352e', marginBottom: '1rem' }}>
                    Derivar mesa <b>{numeroMesa}</b> a
                </h3>

                <select
                    value={seleccionado}
                    onChange={(e) => setSeleccionado(e.target.value)}
                    style={{
                        padding: '0.5rem',
                        fontSize: '1rem',
                        width: '100%',
                        borderRadius: '6px',
                        border: '1px solid #ccc',
                        marginBottom: '1.5rem'
                    }}
                >
                    <option value="">Seleccionar mozo</option>
                    {mozos.map(m => (
                        <option key={m.id} value={m.id}>{m.nombre}</option>
                    ))}
                </select>

                <button
                    className="modal-button"
                    onClick={handleConfirmar}
                    disabled={!seleccionado}
                >
                    Confirmar
                </button>

                <button
                    className="modal-button"
                    onClick={onClose}
                    style={{ backgroundColor: '#999', marginTop: '0.5rem' }}
                >
                    Cancelar
                </button>
            </div>
        </div>
    );
};

export default DerivarMesaModal;
