; ATARI:  ROM compressed file expander

        section base

        xref    end_all
        xref    cmp_dcfl

fx.cbase equ    $40000                   ; code base
fx.sbase equ    $20000                   ; scratch base
memvalid equ    $420
resvalid equ    $426

        move.w  #$2700,sr                ; no interrupts

        clr.l   memvalid
        clr.l   resvalid                 ; invalidate TOS

        lea     fx.cbase,a0              ; decompress to here
        lea     end_all+8,a1             ; start of compressed file
        lea     fx_achp,a2               ; allocate and release code
        bsr.s   cmp_dcfl                 ; decompress
; trap #9
        jmp     fx.cbase

fx_sbase dc.l   fx.sbase
fx_achp
        move.l  fx_sbase,a0
        moveq   #0,d0                    ; dummy allocate
        rts

        tst.l   d0
        rts                              ; dummy release

        end
