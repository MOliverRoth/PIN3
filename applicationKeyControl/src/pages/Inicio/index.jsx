import React, { useState, useEffect } from "react";
import axios from "axios";
import CardChave from "../../components/CardChave";
import TopoGuarda from "../../components/TopoGuarda";
import estilos from "./Inicio.module.css";
import ModalEmprestimo from "../../components/ModalEmprestimo";

function Inicio() {
    const [filter, setFilter] = useState("todos");
    const [searchText, setSearchText] = useState("");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [chaveSelecionada, setChaveSelecionada] = useState(null);
    const [chaves, setChaves] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:8081/visualizar-chaves")
            .then(response => {
                if (Array.isArray(response.data)) {
                    setChaves(response.data);
                } else {
                    console.error("Resposta da API não é um array:", response.data);
                    setChaves([]); 
                }
            })
            .catch(error => {
                console.error("Erro ao carregar as chaves:", error);
                setChaves([]); 
            });
    }, []);

    const chavesFiltradas = Array.isArray(chaves) ? chaves.filter((chave) => {
        if (filter === "emprestadas" && chave.status === "DISPONÍVEL") return false;

        const search = searchText.toLowerCase();
        return (
            chave.codigo.toString().includes(search) ||
            chave.descricao.toLowerCase().includes(search)
        );
    }) : [];

    const abrirModal = (chave) => {
        setChaveSelecionada(chave);
        setIsModalOpen(true);
    };

    const fecharModal = () => {
        setChaveSelecionada(null);
        setIsModalOpen(false);
    };

    const atualizarChaves = () => {
        axios.get("http://localhost:8081/visualizar-chaves")
            .then(response => {
                if (Array.isArray(response.data)) {
                    setChaves(response.data);
                } else {
                    console.error("Resposta da API não é um array:", response.data);
                }
            })
            .catch(error => {
                console.error("Erro ao atualizar as chaves:", error);
            });
    };

    return (
        <div>
            <TopoGuarda />
            <div className={estilos.container}>
                <div className={estilos.searchBar}>
                    <input
                        type="text"
                        placeholder="Buscar chave..."
                        value={searchText}
                        onChange={(e) => setSearchText(e.target.value)}
                        className={estilos.searchInput}
                    />
                    <div className={estilos.statusContainer}>
                        <span
                            className={`${estilos.statusLabel} ${filter === "todos" ? estilos.active : ""}`}
                            onClick={() => setFilter("todos")}
                        >
                            Todas
                        </span>
                        <span
                            className={`${estilos.statusLabel} ${filter === "emprestadas" ? estilos.active : ""}`}
                            onClick={() => setFilter("emprestadas")}
                        >
                            Emprestadas
                        </span>
                    </div>
                </div>

                <hr className={estilos.divisoria} />

                <div className={estilos.cardsGrid}>
                    {chavesFiltradas.map((chave, index) => (
                        <CardChave
                            key={index}
                            number={chave.codigo}
                            title={chave.descricao}
                            status={chave.status === "DISPONÍVEL"}
                            onClick={() => abrirModal(chave)}
                        />
                    ))}
                </div>
            </div>

            {isModalOpen && (
                <ModalEmprestimo
                    fecharModal={fecharModal}
                    chave={chaveSelecionada}
                    atualizarChaves={atualizarChaves}
                    setChaves={setChaves}
                />
            )}
        </div>
    );
}

export default Inicio;
