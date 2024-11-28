import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import TopoAdministrador from "../../components/TopoAdministrador";
import estilos from "./ItemChave.module.css";
import ModalAtribuirPessoa from "../../components/ModalAtribuirPessoa";
import ModalRemoverAtribuicao from "../../components/ModalRemoverAtribuicao";

function ItemChave() {
  const { id } = useParams();
  const [chave, setChave] = useState(null);
  const [chaveOriginal, setChaveOriginal] = useState(null);
  const [editando, setEditando] = useState(false);
  const [usuarios, setUsuarios] = useState([]);
  const [modalAberto, setModalAberto] = useState(false);
  const [termoBusca, setTermoBusca] = useState("");
  const [modalRemoverAberto, setModalRemoverAberto] = useState(false);
  const [usuarioSelecionado, setUsuarioSelecionado] = useState(null);

  const abrirModalRemover = (usuario) => {
    setUsuarioSelecionado(usuario);
    setModalRemoverAberto(true);
  };

  const fecharModalRemover = () => {
    setUsuarioSelecionado(null);
    setModalRemoverAberto(false);
  };

  const confirmarRemocao = () => {
    if (usuarioSelecionado) {
      fetch(`http://localhost:8081/desatribuir-chave?idChave=${id}&idUsuario=${usuarioSelecionado.idUsuario}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => {
          if (response.ok) {
            fetchUsuarios();
          } else {
            throw new Error("Erro ao excluir permissão");
          }
        })
        .catch((error) => console.error("Erro ao excluir permissão:", error))
        .finally(() => fecharModalRemover());
    }
  };

  const handleBuscaChange = (e) => {
    setTermoBusca(e.target.value);
  };

  const usuariosFiltrados = usuarios.filter((usuario) => {
    const termo = termoBusca.toLowerCase();
    return (
      usuario.nome.toLowerCase().includes(termo) ||
      usuario.email.toLowerCase().includes(termo) ||
      usuario.vinculo.toLowerCase().includes(termo) ||
      usuario.cpf.includes(termo)
    );
  });

  useEffect(() => {
    fetch(`http://localhost:8081/visualizar-chave/${id}`)
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("Chave não encontrada");
        }
      })
      .then((data) => {
        setChave(data);
        setChaveOriginal(data);
      })
      .catch((error) => {
        console.error("Erro ao buscar chave:", error);
      });
  }, [id]);

  const fetchUsuarios = () => {
    fetch(`http://localhost:8081/usuarios-chave/${id}`)
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("Erro ao buscar usuários");
        }
      })
      .then((data) => {
        setUsuarios(data);
      })
      .catch((error) => {
        console.error("Erro ao buscar usuários:", error);
      });
  };

  useEffect(() => {
    fetchUsuarios();
  }, [id]);

  if (!chave) {
    return <div>Carregando...</div>;
  }

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setChave({ ...chave, [name]: type === "checkbox" ? checked : value });
  };

  const salvarEdicao = () => {
    if (!window.confirm("Tem certeza que deseja salvar as alterações?")) {
      return;
    }
  
    const chaveDto = {
      codigo: chave.codigo !== undefined ? chave.codigo : chaveOriginal.codigo,
      descricao: chave.descricao !== undefined ? chave.descricao : chaveOriginal.descricao,
      status: chave.status !== undefined ? chave.status : chaveOriginal.status || "ativo",
      horaInicio: chave.horaInicio && chave.horaInicio.length === 5 ? chave.horaInicio + ":00" : chave.horaInicio,
      horaFim: chave.horaFim && chave.horaFim.length === 5 ? chave.horaFim + ":00" : chave.horaFim,
      segunda: chave.segunda !== undefined ? chave.segunda : chaveOriginal.segunda,
      terca: chave.terca !== undefined ? chave.terca : chaveOriginal.terca,
      quarta: chave.quarta !== undefined ? chave.quarta : chaveOriginal.quarta,
      quinta: chave.quinta !== undefined ? chave.quinta : chaveOriginal.quinta,
      sexta: chave.sexta !== undefined ? chave.sexta : chaveOriginal.sexta,
      sabado: chave.sabado !== undefined ? chave.sabado : chaveOriginal.sabado,
      domingo: chave.domingo !== undefined ? chave.domingo : chaveOriginal.domingo,
    };
  
    console.log("Enviando chaveDto:", chaveDto);
  
    fetch(`http://localhost:8081/editar-chave/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(chaveDto),
    })
      .then((response) => {
        if (response.ok) {
          console.log("Chave editada com sucesso:", response);
          return response.json();
        } else {
          return response.text().then((text) => {
            console.log("Erro ao salvar chave:", text);
            throw new Error(text);
          });
        }
      })
      .then((data) => {
        console.log("Dados retornados após edição:", data);
        setChave(data);
        setChaveOriginal(data);
        setEditando(false);
      })
      .catch((error) => {
        console.error("Erro ao salvar edições:", error);
      });
  };
  
    
  const cancelarEdicao = () => {
    setChave(chaveOriginal);
    setEditando(false);
  };

  const abrirModal = () => setModalAberto(true);
  const fecharModal = () => setModalAberto(false);

  return (
    <div>
      <TopoAdministrador />
      <div className={estilos.container}>
        <div className={estilos.tituloContainer}>
          <h1 className={estilos.titulo}>
            Chave {chave.codigo} - {chave.descricao}
          </h1>
          <div>
            <button
                className={estilos.botaoEditar}
                onClick={() => {
                if (editando) salvarEdicao();
                else setEditando(true);
                }}
            >
                {editando ? "Salvar alterações" : "Editar chave"}
            </button>
            {editando && (
                <button className={estilos.botaoCancelar} onClick={cancelarEdicao}>
                Cancelar
                </button>
            )}
          </div>
        </div>

        <hr className={estilos.divisoria} />

        <div className={estilos.form}>
          <div>
            <label className={estilos.label}>ID</label>
            <input
              type="text"
              name="id"
              value={chave.idChave || ""}
              readOnly
              className={estilos.input}
              disabled={true}
            />
          </div>
          <div>
            <label className={estilos.label}>Hora Início</label>
            <input
              type="time"
              name="horaInicio"
              value={chave.horaInicio || ""}
              onChange={handleInputChange}
              className={estilos.input}
              disabled={!editando}
            />
          </div>
          <div>
            <label className={estilos.label}>Código</label>
            <input
              type="text"
              name="codigo"
              value={chave.codigo || ""}
              onChange={handleInputChange}
              className={estilos.input}
              disabled={!editando}
            />
          </div>
          <div>
            <label className={estilos.label}>Hora Fim</label>
            <input
              type="time"
              name="horaFim"
              value={chave.horaFim || ""}
              onChange={handleInputChange}
              className={estilos.input}
              disabled={!editando}
            />
          </div>
          <div>
            <label className={estilos.label}>Descrição</label>
            <input
              type="text"
              name="descricao"
              value={chave.descricao || ""}
              onChange={handleInputChange}
              className={estilos.input}
              disabled={!editando}
            />
          </div>

          <div>
            <label className={estilos.label}>Dias da Semana</label>
            <div className={estilos.diasSemana}>
              {["segunda", "terca", "quarta", "quinta", "sexta", "sabado", "domingo"].map((dia) => (
                <label key={dia}>
                  <input
                    type="checkbox"
                    name={dia}
                    checked={chave[dia] || false}
                    onChange={handleInputChange}
                    disabled={!editando}
                  />
                  {dia.charAt(0).toUpperCase() + dia.slice(1)}
                </label>
              ))}
            </div>
          </div>
        </div>

        <hr className={estilos.divisoria} />

        <div className={estilos.permissoesContainer}>
          <h2>Permissões</h2>
          <button className={estilos.botaoAtribuir} onClick={abrirModal}>
            Atribuir pessoas
          </button>
        </div>

        <div className={estilos.buscaContainer}>
          <input
            type="text"
            value={termoBusca}
            onChange={handleBuscaChange}
            placeholder="Buscar por nome, e-mail, vínculo ou CPF"
            className={estilos.inputBusca}
          />
        </div>

        <table className={estilos.tabela}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Email</th>
              <th>Vínculo</th>
              <th>CPF</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {usuariosFiltrados.length > 0 ? (
              usuariosFiltrados.map((usuario) => (
                <tr key={usuario.idUsuario}>
                  <td>{usuario.idUsuario}</td>
                  <td>{usuario.nome}</td>
                  <td>{usuario.email}</td>
                  <td>{usuario.vinculo}</td>
                  <td>{usuario.cpf}</td>
                  <td>
                    <button
                      className={estilos.acao}
                      onClick={() => abrirModalRemover(usuario)}
                    >
                      <img
                        src="/icon_delete.png"
                        alt="Deletar"
                        className={estilos.icon}
                      />
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6">Nenhum usuário encontrado</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {modalAberto && (
        <div className="modal-overlay">
          <ModalAtribuirPessoa onClose={fecharModal} idChave={id} onPessoaAtribuida={fetchUsuarios} />
        </div>
      )}

      {modalRemoverAberto && (
        <ModalRemoverAtribuicao
          onClose={fecharModalRemover}
          onConfirm={confirmarRemocao}
          permissaoNome={usuarioSelecionado?.nome}
        />
      )}
    </div>
  );
}

export default ItemChave;
