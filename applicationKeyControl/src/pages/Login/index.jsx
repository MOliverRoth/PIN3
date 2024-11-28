import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom"; 
import estilos from "./Login.module.css";

function Login() {
  const [cpf, setCpf] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate(); 

  const handleLogin = async (e) => {
    e.preventDefault(); 
    try {
      const response = await axios.post("http://localhost:8081/login", null, {
        params: {
          cpf: cpf,
          senha: password,
        },
      });

      const userType = response.data.vinculo; 

      if (userType === "vigilante") {
        navigate("/Inicio"); 
      } else if (userType === "administrador") {
        navigate("/Chaves"); 
      } else if (userType === "coordenador de projeto") {
        navigate("/Colaboradores"); 
      } else {
        alert("Usuário não tem permissão para acessar o sistema.");
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        alert("Usuário não encontrado. Verifique suas credenciais.");
      } else {
        alert("Ocorreu um erro ao tentar fazer login. Tente novamente mais tarde.");
      }
    }
  };

  return (
    <div className={estilos.loginPage}>
      <div className={estilos.loginContainer}>
        <div className={estilos.logo}>
          <img
            className={estilos.lockIcon}
            src="/logo.png"
            alt="Ícone de cadeado"
          />
        </div>
        <h1 className={estilos.title}>KeyGuard</h1>
        <form className={estilos.loginForm} onSubmit={handleLogin}>
          <input
            className={estilos.inputField}
            type="text"
            placeholder="CPF"
            value={cpf}
            onChange={(e) => setCpf(e.target.value)}
          />
          <input
            className={estilos.inputField}
            type="password"
            placeholder="Senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button className={estilos.submitBtn} type="submit">
            ENTRAR
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
