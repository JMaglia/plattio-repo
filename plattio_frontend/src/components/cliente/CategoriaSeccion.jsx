import React from 'react';
import PlatoCard from './PlatoCard';
import '../../styles/CategoriaSeccion.css';

const CategoriaSeccion = ({ titulo, platos, pedido, onAgregar, onQuitar }) => {
    return (
        <div className="categoria-seccion">
            <h3 className="categoria-titulo">
                {titulo.charAt(0).toUpperCase() + titulo.slice(1)}
            </h3>
            {platos.map(plato => (
                <PlatoCard
                    key={plato.id}
                    plato={plato}
                    cantidad={pedido[plato.id] || 0}
                    onAgregar={() => onAgregar(plato)}
                    onQuitar={() => onQuitar(plato)}
                />
            ))}
        </div>
    );
};

export default CategoriaSeccion;
