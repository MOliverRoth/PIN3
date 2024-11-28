import React, { useState } from 'react';
import estilos from './ModalEmprestimo.module.css'; 

function ModalEmprestimo({ fecharModal, chave, atualizarChaves }) {
    const [cpf, setCpf] = useState("");
    const [senha, setSenha] = useState("");
    const [status, setStatus] = useState("");
    const [loading, setLoading] = useState(false);

    const emprestarChave = async () => {
        setLoading(true);
        console.log("Iniciando emprestimo...");
        console.log("CPF:", cpf, "Senha:", senha);
        
        try {
            const response = await fetch(`http://localhost:8081/emprestar/${chave.idChave}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    cpf: cpf,
                    senha: senha,
                }),
            });
    
            const data = await response.json();
            console.log("Dados retornados da API:", data);
            
            if (response.ok) {
                console.log("Chave emprestada com sucesso!");
                setStatus("Chave emprestada com sucesso!");
                
                atualizarChaves();                  
                fecharModal();
            } else {
                console.error("Erro ao emprestar chave:", data);
                setStatus(data?.message || "Erro ao emprestar chave.");
            }
        } catch (error) {
            console.error("Erro de conexão:", error);
            setStatus("Erro de conexão.");
        } finally {
            setLoading(false);
        }
    };
    
    

    const devolverChave = async () => {
        setLoading(true);
        console.log("Iniciando devolução...");
        console.log("CPF:", cpf, "Senha:", senha);

        try {
            const response = await fetch(`http://localhost:8081/devolver-chave/${chave.idChave}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    cpf: cpf,
                    senha: senha,
                }),
            });

            const data = await response.json();
            console.log("Dados retornados da API:", data);

            if (response.ok) {
                console.log("Chave devolvida com sucesso!");
                setStatus("Chave devolvida com sucesso!");
                atualizarChaves();  
                fecharModal();
            } else {
                console.error("Erro ao devolver chave:", data);
                setStatus(data?.message || "Erro ao devolver chave.");
            }
        } catch (error) {
            console.error("Erro de conexão:", error);
            setStatus("Erro de conexão.");
        } finally {
            setLoading(false);
        }
    };

    const handleAction = () => {
        console.log("Status da chave:", chave?.status);
        if (chave?.status === "DISPONÍVEL") {
            emprestarChave();
        } else {
            devolverChave();
        }
    };

    return (
        <div className={estilos.modalOverlay}>
            <div className={estilos.modalContent}>
                <div className={estilos.modalHeader}>
                    <h2>
                        {chave?.status === "DISPONÍVEL" ? "Emprestar" : "Devolução"} - {chave?.descricao || "Chave"} - {chave?.codigo || "Número"}
                    </h2>
                    <button className={estilos.fecharBotao} onClick={fecharModal}>
                        &times;
                    </button>
                </div>
                <div className={estilos.modalBody}>
                    <label>CPF</label>
                    <input
                        type="text"
                        placeholder="Digite o CPF"
                        value={cpf}
                        onChange={(e) => setCpf(e.target.value)}
                    />
                    <label>Senha</label>
                    <input
                        type="password"
                        placeholder="Digite a senha"
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                    />
                    <div>{status && <p>{status}</p>}</div>
                </div>
                <div className={estilos.modalFooter}>
                    <button onClick={fecharModal} className={estilos.cancelarBotao}>
                        Cancelar
                    </button>
                    <button onClick={handleAction} className={estilos.emprestarBotao} disabled={loading}>
                        {loading ? "Processando..." : (chave?.status === "DISPONÍVEL" ? "Emprestar" : "Devolver")}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ModalEmprestimo;
