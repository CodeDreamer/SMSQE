; Program a section of PEROM

        section perom

        xdef    pe_prog

        xref    pe_alloc
        xref    pe_setup
        xref    pe_done

        xref    gu_rchp

        include 'dev8_keys_err'
        include 'dev8_keys_atari_perom'

;+++
; This routine should be called in supervisor mode to program a given
; section of PEROM.
;
;       d2 c  p length to program
;       a1 c  p data to program
;       a2 c  p address to program
;
;       status return standard
;
;---
pe_prog
pep.reg reg d2/d3/d4/d6/d7/a0/a1/a2/a3/a4/a5/a6
        movem.l pep.reg,-(sp)
stk_a1a2 equ    6*4

        move.w  #$100,d3                 ; standard sector length

        jsr     pe_alloc                 ; allocate work space
        bne.s   pep_exit

        moveq   #0,d1                    ; set registers for bank 0
        jsr     pe_setup                 ; d6 msw cache flags
                                         ; a3,a4,a5 = normal / address / write
                                         ; a6 address of program code

        move.l  d2,d4                    ; save the length
        add.l   a1,d2                    ; end of program

pep_sloop
        jsr     (a6)                     ; d3 sector length
                                         ; a0 base of programming window
                                         ; d6/a3/a4/a5/a6 as initialised
                                         ; a2 = address in ROM
                                         ; a1 = address in file
        bne.s   pep_done
        cmp.l   d2,a1                    ; all programmed?
        blt.s   pep_sloop                ; ... no

pep_done
        jsr     pe_done                  ; reset
        move.l  d7,a0
        jsr     gu_rchp                  ; return the heap
        bne.s   pep_exit

        movem.l stk_a1a2(sp),a1/a2       ; data and ROM

        subq.l  #1,d4                    ; round down length
        lsr.l   #2,d4

pep_check
        cmpm.l  (a1)+,(a2)+
        dbne    d4,pep_check             ; and check the contents
        beq.s   pep_exit

        moveq   #err.nc,d0               ; error

pep_exit
        movem.l (sp)+,pep.reg
        rts
        end
