; File utilities                        V0.00  1990   Tony Tebby QJUMP

        section gen_util

        include 'dev8_keys_qdos_ioa'

        xdef    gu_fdele
;+++
; Delete a file
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       A0      pointer to filename             preserved
;---
gu_fdele
gfd.reg reg     d1/d3/a0/a1
        movem.l gfd.reg,-(sp)
        moveq   #myself,d1              ; for me
        moveq   #ioa.delf,d0            ; delete something
        trap    #do.ioa
        tst.l   d0
        movem.l (sp)+,gfd.reg
        rts

        end
