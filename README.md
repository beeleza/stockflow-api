# StockFlow API

Sistema de controle de estoque para gestão de produtos, categorias e movimentações.

## Funcionalidades

- **Produtos**: CRUD completo com paginação
- **Categorias**: Organização de produtos
- **Movimentações**: Entrada, saída e ajuste de estoque
- **Controle**: Validação de estoque negativo, produtos ativos apenas
- **Documentação**: OpenAPI/Swagger

## Tecnologias

| Tecnologia | Versão |
|------------|--------|
| Java | 21 |
| Spring Boot | 4.0.5 |
| PostgreSQL | - |
| Spring Data JPA | - |
| SpringDoc OpenAPI | 2.5.0 |
| Lombok | - |

## Getting Started

```bash
./mvnw spring-boot:run
```

Acesse:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## API Endpoints

### Products
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | /product | Listar (paginado) |
| GET | /product/{id} | Buscar por ID |
| POST | /product | Criar |
| PUT | /product/{id} | Atualizar |
| DELETE | /product/{id} | Excluir |

### Categories
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | /category | Listar todos |
| GET | /category/{id} | Buscar por ID |
| POST | /category | Criar |
| PUT | /category/{id} | Atualizar |
| DELETE | /category/{id} | Excluir |

### Stock Movements
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | /stock-movement/entry | Entrada |
| POST | /stock-movement/exit | Saída |
| POST | /stock-movement/adjustment | Ajuste |
| GET | /stock-movement/product/{id} | Histórico |
| GET | /stock-movement/{id} | Buscar por ID |

## Regras de Negócio

- Produto deve estar `ACTIVE` para movimentação
- Quantidade > 0 para entrada/saída
- Ajuste pode ser positivo ou negativo (não zero)
- Estoque negativo não permitido

## Variáveis de Ambiente

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Build

```bash
./mvnw clean package -DskipTests
```

## Testes

```bash
./mvnw test
```

Cobertura atual:
- ProductService: 13 testes
- StockMovementService: 17 testes
- CategoryService: 9 testes