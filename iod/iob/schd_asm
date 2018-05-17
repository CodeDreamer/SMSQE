; Buffering scheduler routine  V2.00    1989  Tony Tebby   QJUMP

        section iou

        xdef    iob_schd

        xref    gu_rchp

        include 'dev8_keys_sys'
        include 'dev8_keys_68000'

;+++
; This scheduler routine returns freed buffers to the heap
;--
iob_schd
        move.w  sr,-(sp)
        or.w    #sr.i7,sr                ; very atomic for this
        move.l  sys_frbl(a6),d0          ; next buffer to free
        beq.s   ibsh_nop
        move.l  d0,a0
        move.l  (a0),sys_frbl(a6)        ; the next after that
ibsh_nop
        move.w  (sp)+,sr
        tst.l   d0
        beq.s   ibsh_exit
        jsr     gu_rchp                  ; return it
        bra.s   iob_schd

ibsh_exit
        rts
        end
