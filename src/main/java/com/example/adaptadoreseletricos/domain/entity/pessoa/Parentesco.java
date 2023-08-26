package com.example.adaptadoreseletricos.domain.entity.pessoa;

public enum Parentesco {
    FILHO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, MAE, PAI);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.MASCULINO;
        }
    },
    FILHA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, MAE, PAI);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.FEMININO;
        }
    },
    PAI {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, FILHA, FILHO);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.MASCULINO;
        }
    },
    MAE {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, FILHA, FILHO);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.FEMININO;
        }
    },
    TIO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, SOBRINHA, SOBRINHO);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.MASCULINO;
        }
    },

    TIA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, SOBRINHA, SOBRINHO);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.FEMININO;
        }
    },
    SOBRINHO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, TIA, TIO);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.MASCULINO;
        }
    },

    SOBRINHA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, TIA, TIO);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.FEMININO;
        }
    },
    IRMAO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, IRMA, IRMAO);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.MASCULINO;
        }
    },

    IRMA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, IRMA, IRMAO);
        }

        @Override
        public Sexo getSexoCorrespondente() {
            return Sexo.FEMININO;
        }
    };


    abstract public Parentesco getInversaoDeParentesco(Sexo sexo);

    abstract public Sexo getSexoCorrespondente();

    private static Parentesco derivaParentescoPorSexo(Sexo sexo,
                                               Parentesco parentescoFeminino,
                                               Parentesco parentescoMasculino){
        if (sexo.equals(Sexo.FEMININO)) return parentescoFeminino;
        return parentescoMasculino;
    }
}
