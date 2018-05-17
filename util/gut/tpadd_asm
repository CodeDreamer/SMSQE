; Thing Procedure Add Procedures      V1.00    1990    Tony Tebby  QJUMP

        section gen_util

        xdef    gu_tpadd

        xref    gu_thuse
        xref    gu_thfre
        xref    prc_name

        include 'dev8_keys_thg'
        include 'dev8_keys_procs'

;+++
; Add some more Thing Extension procedures
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       A1      Pointer to Procedure table      scratch
;---
gu_tpadd
gtpi.reg reg    a0/a2
        movem.l gtpi.reg,-(sp)
        move.l  a1,a2                    ; save pointer

        lea     prc_name,a0
        jsr     gu_thuse                 ; use procedures thing
        bne.s   gtpi_exit

        jsr     prc_addp(a1)

        jsr     gu_thfre                 ; free procedures thing

gtpi_exit
        movem.l (sp)+,gtpi.reg
        rts
        end
