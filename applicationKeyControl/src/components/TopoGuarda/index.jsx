import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; 
import estilos from "./TopoGuarda.module.css";

function TopoGuarda() {
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
                navigate("/"); 
            } else {
                console.error("Erro ao deslogar");
            }
        } catch (error) {
            console.error("Erro na requisição de logout:", error);
        }
    };

    return (
        <header className={estilos.header}>
            <div className={estilos.header__left}>
                <a href="/inicio" className={estilos.header__logo}>KeyGuard</a>
                <nav className={estilos.header__nav}>
                    <a href="/inicio" className={estilos.header__link}>Início</a>
                    <a href="/relatorio" className={estilos.header__link}>Relatórios</a>
                </nav>
            </div>
            <div className={estilos.header__right}>
                <button className={estilos.header__icon}>
                    <img src="icon_notificacao.png" alt="Notificações" />
                </button>
                <div className={estilos.header__profile}>
                    <button 
                        className={estilos.header__icon} 
                        onClick={toggleDropdown}>
                        <img src="icon_perfil.png" alt="Perfil" />
                    </button>
                    {isDropdownOpen && (
                        <div className={estilos.dropdown}>
                            <button 
                                className={estilos.dropdown__item} 
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

export default TopoGuarda;
