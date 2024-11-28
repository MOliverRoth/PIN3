import React, { useState, useEffect } from "react";
import axios from "axios"; 
import TopoAdministrador from "../../components/TopoAdministrador";
import ModalAddChave from "../../components/ModalAddChave";
import ModalRemoverChave from "../../components/ModalRemoverChave";
import estilos from "./Chave.module.css";
import { useNavigate } from "react-router-dom";

function Chave() {
    const navigate = useNavigate();

    const handleEditarChave = (id) => {
        navigate(`/chave/${id}`);
    };    

    const [busca, setBusca] = useState("");
    const [chavesFiltradas, setChavesFiltradas] = useState([]);
    const [chavesOriginais, setChavesOriginais] = useState([]);
    const [modalAberto, setModalAberto] = useState(false);
    const [modalRemoverAberto, setModalRemoverAberto] = useState(false);
    const [chaveParaExcluir, setChaveParaExcluir] = useState(null);

    useEffect(() => {
        axios.get("http://localhost:8081/visualizar-chaves")
            .then((response) => {
                setChavesOriginais(response.data); 
                setChavesFiltradas(response.data); 
            })
            .catch((error) => {
                console.error("Erro ao carregar as chaves", error);
            });
    }, []);

    const handleCreateChave = (novaChave) => {
        axios
            .post("http://localhost:8081/criar-chave", novaChave)
            .then((response) => {
                setChavesOriginais([...chavesOriginais, response.data]);
                setChavesFiltradas([...chavesFiltradas, response.data]);
                setModalAberto(false);
            })
            .catch((error) => {
                console.error("Erro ao criar a chave", error);
                alert("Não foi possível criar a chave. Verifique os dados e tente novamente.");
            });
    };

    const handleBusca = (e) => {
        const termoBusca = e.target.value.toLowerCase();
        setBusca(termoBusca);
            
        const resultadosFiltrados = chavesOriginais.filter((chave) => {
            return (
                chave.codigo.toString().includes(termoBusca) || 
                chave.descricao.toLowerCase().includes(termoBusca)
            );
        });
    
        setChavesFiltradas(resultadosFiltrados);
    };
    
    
    const abrirModal = () => setModalAberto(true);
    const fecharModal = () => setModalAberto(false);

    const abrirModalRemover = (chave) => {
        setChaveParaExcluir(chave);
        setModalRemoverAberto(true);
    };

    const fecharModalRemover = () => {
        setModalRemoverAberto(false);
        setChaveParaExcluir(null);
    };

    const handleExcluirChave = () => {
        axios
            .delete(`http://localhost:8081/deletar-chave/${chaveParaExcluir.idChave}`)
            .then((response) => {
                const chavesAtualizadas = chavesFiltradas.filter(
                    (chave) => chave.idChave !== chaveParaExcluir.idChave
                );
                setChavesFiltradas(chavesAtualizadas);
                fecharModalRemover();
                alert("Chave excluída com sucesso!");
            })
            .catch((error) => {
                console.error("Erro ao excluir a chave", error);
                alert("Não foi possível excluir a chave. Tente novamente.");
            });
    };
    

    return (
        <div>
            <TopoAdministrador />
            <div className={estilos.container}>
                <div className={estilos.conteudo}>
                    <h1 className={estilos.titulo}>Chaves</h1>
                    <div className={estilos.topBar}>
                        <input
                            type="text"
                            placeholder="Buscar chave..."
                            className={estilos.busca}
                            value={busca}
                            onChange={handleBusca}
                        />
                        <button
                            className={estilos.botaoAdicionar}
                            onClick={abrirModal}
                        >
                            Nova chave
                        </button>
                    </div>

                    <hr className={estilos.divisoria} />

                    <table className={estilos.tabela}>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Código</th>
                                <th>Descrição</th>
                                <th>Hora Início</th>
                                <th>Hora Fim</th>
                                <th>Dias da Semana</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            {chavesFiltradas.map((chave) => (
                                <tr key={chave.idChave}>
                                    <td>{chave.idChave}</td>
                                    <td>{chave.codigo}</td>
                                    <td>{chave.descricao}</td>
                                    <td>{chave.horaInicio}</td>
                                    <td>{chave.horaFim}</td>
                                    <td>
                                        {chave.segunda && "Segunda "}
                                        {chave.terca && "Terça "}
                                        {chave.quarta && "Quarta "}
                                        {chave.quinta && "Quinta "}
                                        {chave.sexta && "Sexta "}
                                        {chave.sabado && "Sábado "}
                                        {chave.domingo && "Domingo "}
                                    </td>
                                    <td>
                                        <button
                                            className={estilos.acao}
                                            onClick={() => handleEditarChave(chave.idChave)}
                                        >
                                            <img
                                                src="/icon_edit.png"
                                                alt="Editar"
                                                className={estilos.icon}
                                            />
                                        </button>
                                        <button
                                            className={estilos.acao}
                                            onClick={() => abrirModalRemover(chave)}
                                        >
                                            <img
                                                src="/icon_delete.png"
                                                alt="Deletar"
                                                className={estilos.icon}
                                            />
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <p className={estilos.resultados}>
                        {`Total de resultados: ${chavesFiltradas.length}`}
                    </p>
                </div>
            </div>

            {modalAberto && (
                <ModalAddChave
                    onClose={fecharModal}
                    onCreate={handleCreateChave}
                />
            )}

            {modalRemoverAberto && (
                <ModalRemoverChave
                    onClose={fecharModalRemover}
                    onConfirm={handleExcluirChave}
                    permissaoNome={chaveParaExcluir?.codigo}
                />
            )}
        </div>
    );
}

export default Chave;
