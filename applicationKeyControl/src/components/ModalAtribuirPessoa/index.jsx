import React, { useEffect, useState } from 'react';
import axios from 'axios';
import estilos from './ModalAtribuirPessoa.module.css';

function ModalAtribuirPessoa({ onClose, idChave, onPessoaAtribuida }) {
    const [usuarios, setUsuarios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedUsuario, setSelectedUsuario] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        axios
            .get('http://localhost:8081/usuarios')
            .then((response) => {
                setUsuarios(response.data);
                setLoading(false);
            })
            .catch((error) => {
                setError('Erro ao carregar os usuários. Tente novamente mais tarde.');
                console.error('Erro ao carregar os usuários:', error);
                setLoading(false);
            });
    }, []);

    const handleSearch = (e) => setSearchTerm(e.target.value);

    const filteredUsuarios = usuarios.filter((usuario) =>
        usuario.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
        usuario.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
        usuario.cpf.includes(searchTerm)
    );

    const handleAtribuir = () => {
        if (selectedUsuario) {
            axios
                .post(`http://localhost:8081/atribuir-chave?idChave=${idChave}`, selectedUsuario.idUsuario, {
                    headers: { 'Content-Type': 'application/json' },
                })
                .then(() => {
                    console.log('Usuário atribuído com sucesso:', selectedUsuario);
                    if (onPessoaAtribuida) {
                        onPessoaAtribuida();
                    }
                    onClose();
                })
                .catch((error) => {
                    console.error('Erro ao atribuir o usuário:', error);
    
                    console.log('Resposta do erro:', error.response);
    
                    const errorMessage =
                        (error.response && error.response.data && error.response.data.message) ||
                        (error.response && error.response.data && error.response.data.error) ||
                        'Erro ao atribuir o usuário. Tente novamente.';
    
                    setError(errorMessage);
                });
        }
    };
    

    const handleCancel = () => {
        setSelectedUsuario(null);
        setError('');
        onClose();
    };
    
    const handleSelectUsuario = (usuario) => {
        setSelectedUsuario(usuario);
        setError(''); 
    };
    


    if (loading) {
        return (
            <div className={estilos.modalOverlay}>
                <div className={estilos.modal}>
                    <p>Carregando usuários...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className={estilos.modalOverlay}>
                <div className={estilos.modal}>
                    <p>{error}</p>
                    <button onClick={onClose} className={estilos.cancelButton}>Fechar</button>
                </div>
            </div>
        );
    }

    return (
        <div className={estilos.modalOverlay}>
            <div className={estilos.modal} role="dialog" aria-label="Atribuir usuário">
                <h2>Usuários</h2>
                <input
                    type="text"
                    placeholder="Buscar por nome, email ou CPF"
                    value={searchTerm}
                    onChange={handleSearch}
                    className={estilos.searchInput}
                    aria-label="Campo de busca"
                />
                <ul>
                    {filteredUsuarios.map((usuario) => (
                        <li
                            key={usuario.idUsuario}
                            onClick={() => handleSelectUsuario(usuario)}
                            className={
                                selectedUsuario?.idUsuario === usuario.idUsuario
                                    ? estilos.selected
                                    : ''
                            }
                        >
                            {usuario.nome} - {usuario.email} - {usuario.cpf}
                        </li>
                    ))}
                </ul>

                {error && <p className={estilos.error}>{error}</p>}

                <div className={estilos.buttons}>
                    <button onClick={handleCancel} className={estilos.cancelButton}>
                        Cancelar
                    </button>
                    <button
                        onClick={handleAtribuir}
                        className={estilos.atribuirButton}
                        disabled={!selectedUsuario}
                    >
                        Atribuir
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ModalAtribuirPessoa;