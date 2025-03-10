# Thymeleaf Task Manager
Sistema de gerenciamento de tarefas com Spring Security e Thymeleaf

## Acesso do sistema
Pode ser feito através do endpoint raiz ou em "/home"

## Usuários base para testes
- admin@fiap.com / admin
- manager@fiap.com / mngr
- collaborator@fiap.com / cola

## Progresso com o projeto
Como ponto de atenção, nem todas as funcionalidades estão implementadas
Até o momento, as seguintes features estão implementadas:
- Endpoints de autenticação (/register, /login e logout);
- Liberação de rotas com base no User Role;
- HomeScreen personalizada para cada user role;
- Templates para todas as operações CRUD para Tarefas.

Os endpoints para gerenciamento de usuários, exibição de dados do usuário e relatórios não foram implementados.
