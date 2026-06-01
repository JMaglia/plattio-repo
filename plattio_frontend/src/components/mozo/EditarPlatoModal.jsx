import React, { useEffect, useState } from 'react';
import '../../styles/EditarPlatoModal.css';
import Swal from 'sweetalert2';


const EditarPlatoModal = ({ id, onClose, onGuardado, esNuevo = false, categoriaDefault = "" }) => {
  const [plato, setPlato] = useState(null);

  useEffect(() => {
    if (!esNuevo) {
      fetch(`http://localhost:8080/platos/${id}`)
        .then(res => res.json())
        .then(data => setPlato(data))
        .catch(err => console.error(err));
    } else {
      setPlato({
        nombre: '',
        descripcion: '',
        precio: '',
        categoria: categoriaDefault,
        tiempoEstimado: 10
      });
    }
  }, [id, esNuevo, categoriaDefault]);

  const handleChange = (e) => {
    setPlato({ ...plato, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const url = esNuevo
      ? `http://localhost:8080/platos`
      : `http://localhost:8080/platos/${id}/actualizar`;

    const method = 'POST';
    const headers = { 'Content-Type': esNuevo ? 'application/json' : 'application/x-www-form-urlencoded' };

    const body = esNuevo
      ? JSON.stringify(plato)
      : new URLSearchParams({
        nombre: plato.nombre,
        descripcion: plato.descripcion,
        precio: plato.precio,
        categoria: plato.categoria,
        tiempoEstimado: plato.tiempoEstimado || 10
      }).toString();

    fetch(url, { method, headers, body })
      .then(res => {
        if (res.ok) {
          Swal.fire({
            icon: 'success',
            title: esNuevo ? 'Plato creado correctamente' : 'Plato actualizado correctamente',
            confirmButtonText: 'Aceptar',
            customClass: {
              confirmButton: 'swal-btn-custom'
            },
            buttonsStyling: false
          }).then(() => {
            onGuardado();
            onClose();
          });
        } else {
          return res.text().then(text => {
            console.error("Error:", text);
            alert("Error al guardar");
          });
        }
      })
      .catch(err => {
        console.error("Error en la solicitud:", err);
        alert("Error de red");
      });
  };

  if (!plato) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>{esNuevo ? 'Agregar plato' : 'Editar plato'}</h3>
        <form onSubmit={handleSubmit}>
          <label>Nombre:</label>
          <input name="nombre" value={plato.nombre} onChange={handleChange} required />

          <label>Descripción:</label>
          <textarea name="descripcion" value={plato.descripcion} onChange={handleChange} />

          <label>Precio:</label>
          <input name="precio" type="number" value={plato.precio} onChange={handleChange} required />

          {!esNuevo ? (
            <>
              <label>Categoría:</label>
              <select name="categoria" value={plato.categoria} onChange={handleChange} required>
                <option value="entrada">Entrada</option>
                <option value="principal">Principal</option>
                <option value="bebida">Bebida</option>
                <option value="postre">Postre</option>
              </select>
            </>
          ) : (
            <p style={{ fontStyle: 'italic', marginTop: '-0.5rem', marginBottom: '1rem' }}>
              Categoría: <strong>{categoriaDefault}</strong>
            </p>
          )}

          <div className="modal-actions">
            <button type="submit">{esNuevo ? 'Agregar' : 'Guardar'}</button>
            <button type="button" onClick={onClose}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditarPlatoModal;
