; HOTKEY Open console   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_cons

        xref    gu_fopen

        include 'dev8_keys_qdos_ioa'

cons    dc.w    15,'con_512x100a0x0'
;+++
; This routine opens a console.
;
;       a0  r   channel id
;       error returns standard
;---
hk_cons
        movem.l d2/d3,-(sp)
        moveq   #ioa.kexc,d3
        lea     cons,a0
        jsr     gu_fopen
        movem.l (sp)+,d2/d3
        rts
        end
