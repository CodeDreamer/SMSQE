; PAR/SER Extension Procedure Initialisation     1990  Tony Tebby   QJUMP

        section exten

        xdef    par_xinit

        xref    par_xprocs
        xref    gu_tpadd
;+++
; Add extensions procedures for CLI.
;
;       status return standard
;---
par_xinit
pxi.reg reg    a1
        move.l  a1,-(sp)

        lea     par_xprocs,a1
        jsr     gu_tpadd

        move.l  (sp)+,a1
        rts
        end
