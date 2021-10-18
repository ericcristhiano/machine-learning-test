# Machine Learning

Esta é uma aplicação utilizada para extrair características e classificar imagens em limão ou laranja. As características utilizadas para classificação foram: As cores laranja e verde para a laranja e as cores verde e amarelo para o limão.

## Instruções

### Como extrair as características

1. Na pasta `src/imagens` há as imagens de treinamento. Imagens iniciando com a letra `l` são consideradas limão e qualquer outra letra são consideradas laranja. Adicione/remova imagens conforme necessário.
2. Execute o arquivo `Main.java` para iniciar a aplicação.
3. Clique em `Extração de características` e aguarde a finalização do processo.
4. Finalizado o processo, haverá um novo arquivo na raíz do projeto chamado `caracteristicas.arff`.

### Como classificar uma imagem

1. Execute o arquivo `Main.java` para iniciar a aplicação.
2. Clique em carregar imagem e selecione uma image de limão ou laranja.
3. As características da imagem extraída serão mostradas no menu características ao lado direito da interface.
4. Clique em classificar para executar o Naive Bayes.
5. A porcentagem da probabilidade de cada uma das classes será mostrado no menu Classificação por Aprendizagem Bayesiana (Naive Bayes) ao lado direito da interface.

### Matriz de confusão obtida com o dataset treinado

| a  | b  | <-- classified as |
|----|----|-------------------|
| 64 | 4  | a = Limão         |
| 6  | 42 | b = Laranja       |
