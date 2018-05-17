; Set the length of a concatenated boot file
;
        section main

        xdef    boot_setlen

;+++
; This processes the length table at the start of a concatenated boot file,
; finding the true length and adding this to all required addresses.
;
;       d2 c  u length of adjusted concatenated file
;       a1 c  u ptr concatenated file (length of 1st file / start of 1st file)
;
;       status return zero
;
;---
boot_setlen
        addq.l  #8,a1                    ; skip length and checksum of first file
        subq.l  #8,d2                    ; the length needs to be adjusted too

        cmp.w   #$fafa,(a1)              ; flagged?
        bne.s   bs_rts                   ; ... no
        subq.l  #4,d2                    ; the length needs further adjustment
        addq.l  #2,a1                    ; skip flag
        move.w  (a1)+,d0                 ; number of addresses to blat
        beq.s   bs_rts

        move.l  a3,-(sp)
        lsl.l   #2,d0                    ; size of address table
        sub.l   d0,d2                    ; corrected
bs_loop
        move.l  a1,a3
        add.l   (a1)+,a3                 ; address to blat
        add.l   d2,(a3)
        subq.l  #4,d0                    ; another
        bgt.s   bs_loop

        move.l  (sp)+,a3
bs_rts
        moveq   #0,d0
        rts

        end
