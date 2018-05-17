; ATARI send command     1988  Tony Tebby  QJUMP

        section fd

        xdef    fd_cmd                  ; send command to FDC

        include dev8_keys_atari

*+++
; This routine sends a command to the floppy disk controller
;
;       d0 c s  command number
;
;       status return arbitrary
;
*---
fd_cmd
        move.w  #dma.fctl,dma_mode       ; set control reg
        move.w  d0,dma_data              ; and command

        move.l  d1,-(sp)
        moveq   #18,d1                   ; wait up to 60 us for busy
fdc_wbusy
        moveq   #1<<fds..bsy,d0          ;              04
        and.w   dma_data,d0              ; busy yet?    12
        dbne    d1,fdc_wbusy             ;              10
                                         ;              26=3.25us
        ror.l   #8,d0                    ; wait for drq to be stable
        move.l  (sp)+,d1
        rts
        end
