import pandas as pd
import numpy as np
import joblib # Usaremos joblib para salvar o modelo (é melhor que pickle para scikit-learn)
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score

# --- 1. GERAÇÃO DE DADOS SIMULADOS ---
# (Aqui criamos o dataset que definimos)

print("Iniciando geração de dados simulados...")

# Definindo o número de amostras
n_samples = 2000
np.random.seed(42) # Para resultados reproduzíveis

# Criando as features (X)
data = {
    'amount': np.random.lognormal(mean=3.0, sigma=1.0, size=n_samples).round(2),
    'time_since_last_tx': np.random.uniform(low=0.1, high=1000, size=n_samples).round(2),
    'sender_risk_score': np.random.uniform(low=0.01, high=0.99, size=n_samples).round(2),
    'recipient_is_new': np.random.randint(low=0, high=2, size=n_samples) # 0 ou 1
}

# Criando o DataFrame (a "tabela" de dados)
df = pd.DataFrame(data)

# Criando o Target (y) - a coluna 'is_fraud'
# Vamos simular que a fraude é mais provável com valores (amount) altos e risco (sender_risk_score) alto
# Esta é uma regra de negócio simples para criar dados de exemplo
df['is_fraud'] = (
    (df['amount'] > 150) & (df['sender_risk_score'] > 0.7) | 
    (df['amount'] > 500)
).astype(int) # Converte de True/False para 1/0

print(f"Dataset criado com {n_samples} amostras.")
print(f"Total de fraudes simuladas: {df['is_fraud'].sum()}")


# --- 2. PREPARAÇÃO E TREINAMENTO DO MODELO ---

print("Iniciando treinamento do modelo de ML...")

# Definindo X (features) e y (target)
features = ['amount', 'time_since_last_tx', 'sender_risk_score', 'recipient_is_new']
target = 'is_fraud'

X = df[features]
y = df[target]

# Dividindo dados para treino e teste (80% treino, 20% teste)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Inicializando o modelo (Regressão Logística é ótima para classificação binária)
model = LogisticRegression()

# Treinando o modelo
model.fit(X_train, y_train)

print("Modelo treinado com sucesso.")


# --- 3. AVALIAÇÃO (Opcional, mas bom para o portfólio) ---

y_pred = model.predict(X_test)
acc = accuracy_score(y_test, y_pred)
print(f"Acurácia do modelo no teste: {acc * 100:.2f}%")


# --- 4. SALVANDO O MODELO (Persistência) ---
# (Este é o passo CRÍTICO para a API)

model_filename = 'fraud_model.joblib'
joblib.dump(model, model_filename)

print(f"Modelo salvo com sucesso como '{model_filename}'!")