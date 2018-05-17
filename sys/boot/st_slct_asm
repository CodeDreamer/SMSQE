; ATARI select drive/side      1988  Tony Tebby  QJUMP

        section fd

        xdef    fd_slct                 ; select
        xdef    fd_dslct                ; deselect

        include dev8_keys_atari

*+++
; Deselect drive and side
;
;       d0   s  scratch
;
;       status return arbitrary
;
*---
fd_dslct
        movem.l d1/d2,-(sp)

fdds_wait
        move.w  #1<<fds..mo,d1
        and.w   dma_data,d1
        bne.s   fdds_wait

        clr.w   d1                       ; clear drive / side
        clr.w   d2
        bsr.s   fd_slct                  ; select
        movem.l (sp)+,d1/d2
        rts

*+++
; Select disk drive and side
;
;       d0   s  scratch
;       d1 c  p drive   1=drive A   2=drive B
;       d2 c  p side    0 or 1
;
;       status return arbitrary
*---
fd_slct
        move.w  sr,-(sp)
        or.w    #$0700,sr                ; atomic

        move.b  #fdc.ctls,fdc_ctls       ; select control register
        moveq   #fdc.bits,d0
        or.b    fdc_ctlr,d0              ; fdc bits high
        eor.b   d2,d0                    ; set side
        bclr    d1,d0                    ; set drive
        move.b  d0,fdc_ctlw              ; and write to port

        move.w  (sp)+,sr
        rts
        end
