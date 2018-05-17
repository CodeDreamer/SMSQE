* Memory manager linked list maintenance   V2.00   Tony Tebby  QJUMP
*
        section mem
*
        xdef    mem_llst        link into list
*
*       a0 c  p new link address
*       a1 c  p pointer to previous item (or start of list)
*
*       all other registers preserved
*       no error return, condition codes arbitrary
*
mem_llst
        move.l  (a1),(a0)               transfer link pointer to new link
        move.l  a0,(a1)                 set link to new item
        rts
        end
