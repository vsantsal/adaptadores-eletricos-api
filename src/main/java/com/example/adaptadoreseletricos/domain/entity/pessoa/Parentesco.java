package com.example.adaptadoreseletricos.domain.entity.pessoa;

public enum Parentesco {
    FILHO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, MAE, PAI);
        }
    },
    FILHA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, MAE, PAI);
        }
    },
    PAI {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, FILHA, FILHO);
        }
    },
    MAE {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, FILHA, FILHO);
        }
    },
    TIO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, SOBRINHA, SOBRINHO);
        }
    },

    TIA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, SOBRINHA, SOBRINHO);
        }
    },
    AVO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, NETA, NETO);
        }
    },
    SOBRINHO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, TIA, TIO);
        }
    },

    SOBRINHA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, TIA, TIO);
        }
    },
    IRMAO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, IRMA, IRMAO);
        }
    },

    IRMA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, IRMA, IRMAO);
        }
    },
    NETO {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, AVO, AVO);
        }
    },
    NETA {
        @Override
        public Parentesco getInversaoDeParentesco(Sexo sexo) {
            return derivaParentescoPorSexo(sexo, AVO, AVO);
        }
    };


    abstract public Parentesco getInversaoDeParentesco(Sexo sexo);

    private static Parentesco derivaParentescoPorSexo(Sexo sexo,
                                               Parentesco parentescoFeminino,
                                               Parentesco parentescoMasculino){
        if (sexo.equals(Sexo.FEMININO)) return parentescoFeminino;
        return parentescoMasculino;
    }
}
