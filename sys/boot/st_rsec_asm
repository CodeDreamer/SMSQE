; ATARI read sector      1988   Tony Tebby  QJUMP

        section fd

        xdef    fd_rsec

        xref    fd_cmd

        include dev8_keys_atari

*+++
; Read sector - direct physical version
;
;       d0  r   error code
;       a1 c  p sector address
;       d4 c  p sector number
;
;       status return zero
*---
fd_rsec
        move.w  #dma.fsct,dma_mode       ; set sector reg
        move.w  d4,dma_data              ; and sector required

fdr_rtry
        move.l  a1,d0
        move.b  d0,dma_low               ; set dma address
        lsr.w   #8,d0
        move.b  d0,dma_mid
        swap    d0
        move.b  d0,dma_high

        move.w  #dma.frct,dma_mode
        move.w  #dma.fwct,dma_mode       ; initialise DMA
        move.w  #dma.frct,dma_mode
        move.w  #$1,dma_scnt             ; one sector only

        moveq   #fdc.rsec,d0             ; read sector
        jsr     fd_cmd
fdr_wait
        btst    #mfp..dsk,mfp_dsk        ; done?
        bne.s   fdr_wait

        moveq   #1<<dms..ok,d0           ; dma status
        and.w   dma_stat,d0
        beq.s   fdr_rtry                 ; not properly terminated
        move.w  #dma.fstt,dma_mode
        moveq   #1<<fds..bsy+1+1<<fds..los+1<<fds..crc+1<<fds..rnf,d0 ;fdc err?
        and.w   dma_data,d0
        bne.s   fdr_rtry                 ; ... yes
fds_exit
        rts
        end
