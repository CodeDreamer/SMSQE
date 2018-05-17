; QL Arithmetic (Rel A6 to absolute) Operations    V2.01   1990  Tony Tebby  QJUMP

        section qa

rel     macro   routine
        xdef    qr_[routine]
        xref    qa_[routine]
qr_[routine]
        add.l   a6,a1
        jsr     qa_[routine]
        sub.l   a6,a1
        rts
        endm

        rel     nint
        rel     nlint
        rel     float
        rel     pushd
        rel     dup
        rel     neg
        rel     addd
        rel     mul
        rel     muld
        rel     div
        rel     divd
        rel     power

        end
