import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginEmpleado = () => {
    const [email, setEmail] = useState('');
    const [rol, setRol] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = () => {
        if (!email || !rol) {
            setError("Completar todos los campos");
            return;
        }

        fetch(`http://localhost:8080/empleados/email/${email}`)
            .then(res => res.ok ? res.json() : null)
            .then(data => {
                if (!data) {
                    setError("Empleado no encontrado");
                    return;
                }

                if (data.rol !== rol) {
                    setError("Rol incorrecto");
                    return;
                }

                // Guardar empleado en localStorage
                localStorage.setItem("empleado", JSON.stringify(data));

                // Redirigir según rol
                if (rol === "mozo") navigate(`/mozo/${data.id}`);
                else if (rol === "cocinero") navigate('/cocina');
                else setError("Rol no soportado aún");
            })
            .catch(() => setError("Error de conexión"));
    };

    return (
        <div style={{ padding: "2rem", maxWidth: "400px", margin: "0 auto", textAlign: "center" }}>
            <h2>Login de empleado</h2>

            <input
                type="text"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                style={{ width: "100%", padding: "0.5rem", marginBottom: "1rem" }}
            />

            <select
                value={rol}
                onChange={(e) => setRol(e.target.value)}
                style={{ width: "100%", padding: "0.5rem", marginBottom: "1rem" }}
            >
                <option value="">Seleccionar rol</option>
                <option value="mozo">Mozo</option>
                <option value="cocinero">Cocinero</option>
                {/* Agregás más roles si querés */}
            </select>

            {error && <p style={{ color: "red" }}>{error}</p>}

            <button onClick={handleLogin} style={{ padding: "0.6rem 1rem", backgroundColor: "#d3352e", color: "white", border: "none", borderRadius: "6px" }}>
                Ingresar
            </button>
        </div>
    );
};

export default LoginEmpleado;
