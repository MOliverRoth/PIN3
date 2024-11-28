import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import estilos from "./TopoAdministrador.module.css";

function TopoAdministrador() {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const navigate = useNavigate();

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    const handleLogout = async () => {
        try {
            const response = await fetch("http://localhost:8081/sair", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                alert("Usuário deslogado com sucesso!");
                setIsDropdownOpen(false); 
                navigate("/"); 
            } else {
                const errorMessage = await response.text();
                console.error("Erro ao deslogar:", errorMessage);
            }
        } catch (error) {
            console.error("Erro na requisição de logout:", error);
        }
    };

    return (
        <header className={estilos.headerAdministrador}>
            <div className={estilos.header__leftAdministrador}>
                <a href="/chaves" className={estilos.header__logoAdministrador}>KeyGuard</a>
                <nav className={estilos.header__navAdministrador}>
                    <a href="/chaves" className={estilos.header__linkAdministrador}>Chaves</a>
                    <a href="/relatorioAdm" className={estilos.header__linkAdministrador}>Relatórios</a>
                </nav>
            </div>
            <div className={estilos.header__rightAdministrador}>
                <div className={estilos.header__profileAdministrador}>
                    <button
                        className={estilos.header__iconAdministrador}
                        onClick={toggleDropdown}>
                        <img src="/icon_perfil.png" alt="Perfil" />
                    </button>
                    {isDropdownOpen && (
                        <div className={estilos.dropdownAdministrador}>
                            <button
                                className={estilos.dropdown__itemAdministrador}
                                onClick={handleLogout}>
                                Sair
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </header>
    );
}

export default TopoAdministrador;
