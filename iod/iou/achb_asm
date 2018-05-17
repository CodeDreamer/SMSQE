; IO Utilities Allocate Channel Block   V2.00   1989   Tony Tebby QJUMP

        section iou

        xdef    iou_achb

        include 'dev8_keys_qlv'
;+++
; IO Utilities Allocate Channel Block
;
;       d0 cr   required length / status
;       a0  r   base of block
;       all other registers preserved
;---
iou_achb
ioa.reg  reg    d1-d3/a1-a3
        movem.l ioa.reg,-(sp)
        move.l  d0,d1
        move.w  mem.achp,a2
        jsr     (a2)
ioa_exit
        movem.l (sp)+,ioa.reg
        rts
        end
