; Procedure to call function        1988   Tony Tebby   QJUMP

        section hotkey

        xdef    ert

        xref    ut_gxli1

ert
        jsr     ut_gxli1                 ; exactly one long integer
        bne.s   ert_rts                  ; ... oops
        move.l  (a6,a1.l),d1             ; error?
        bge.s   ert_rts                  ; ... no
        move.l  d1,d0                    ; ... yes, set it
ert_rts
        rts
        end
